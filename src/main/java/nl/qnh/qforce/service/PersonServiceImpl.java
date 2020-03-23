package nl.qnh.qforce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.qnh.qforce.config.QforceConfig;
import nl.qnh.qforce.domain.*;
import nl.qnh.qforce.repository.AnalyticsRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.rmi.ConnectIOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static nl.qnh.qforce.domain.AnalyticsVerb.FOUND;
import static nl.qnh.qforce.domain.AnalyticsVerb.YIELDED;

/**
 * The person service implementation to search and retrieve persons.
 * <br/><br/>
 * <b>requests</b><br/>
 * Use javas immutable {@link HttpClient} to execute the http requests, which are simple GET requests and only need the
 * <i>Accept</i> header to receive the correct data format (JSON). Subsequently, the Jackson {@link ObjectMapper} is
 * leveraged to deserialize the JSON data into a populated {@link PersonImpl}.
 * <br/><br/>
 * <b>search</b><br/>
 * Once the first {@link PersonSearchResultPage} is received, subsequent pages are fetched asynchronously for improved performance in the
 * case of multiple pages.
 * <br/><br/>
 * <b>by id</b><br/>
 * When person data is requested by id, the result contains the location of movie data, which
 * will be requested afterwards and added to the {@link PersonImpl} instance.
 * <br/><br/>
 * <b>cache</b><br/>
 * Fetched Persons and Movies are cached in an ordered and thread safe {@link ConcurrentSkipListMap}, because we expect
 * a lot of requests! The java return type is inferred from the request url, so we can use its #computeIfAbsent method
 * which accepts only Function and not BiFunction unfortunately. We then could've passed the return type explicitly, I
 * accepted use of {@link Map#computeIfAbsent} with {@link #inferType(String)}.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    private final HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder().setHeader("Accept", "application/json");
    private final ConcurrentNavigableMap<String, Object> cache = new ConcurrentSkipListMap<>();

    private final ObjectMapper objectMapper;
    private final QforceConfig config;
    private final AnalyticsRepository analyticsRepository;

    public PersonServiceImpl(ObjectMapper objectMapper, QforceConfig config, AnalyticsRepository analyticsRepository) {
        this.objectMapper = objectMapper;
        this.config = config;
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public List<Person> search(String query) {

        if (LOG.isDebugEnabled())
            LOG.debug("Search request received with query: \"{}\"", query);

        final List<Person> result = new ArrayList<>();

        final String queryUrl = config.getSwapiPeopleSearchUrl() + query;
        final PersonSearchResultPage firstPage = fetchResource(queryUrl, PersonSearchResultPage.class);

        if (firstPage != null) {

            analyticsRepository.save(new AnalyticsEntry(queryUrl, FOUND, Integer.toString(firstPage.getCount())));

            Stream.of(firstPage.getResults(), getRemainingPages(firstPage, queryUrl))
                    .flatMap(Collection::stream)
                    .forEach(person -> {
                        populateMovies(person);
                        cache.putIfAbsent(config.getSwapiPeopleUrl() + person.getId(), person);
                        result.add(person);
                    });
        }

        return result;
    }

    @Override
    public Optional<Person> get(long id) {

        if (LOG.isDebugEnabled())
            LOG.debug("Person request received with id: \"{}\"", id);

        final String url = config.getSwapiPeopleUrl() + id;
        final PersonImpl person = (PersonImpl) cache.computeIfAbsent(url, s -> fetchResource(s, inferType(s)));

        if (person == null) {
            analyticsRepository.save(new AnalyticsEntry(url, YIELDED, null));
            return Optional.empty();
        } else {
            analyticsRepository.save(new AnalyticsEntry(url, YIELDED, PersonImpl.class.getSimpleName()));
        }

        return Optional.of(populateMovies(person));
    }

    private PersonImpl populateMovies(PersonImpl person) {

        final List<Movie> movies = person.getMovieUrls().stream()
                .map(url -> (MovieImpl) cache.computeIfAbsent(url,
                        s -> fetchResource(s, inferType(s))))
                .collect(Collectors.toList());

        person.setMovies(movies);

        return person;
    }

    private <T> T fetchResource(String url, Class<T> resourceType) {

        final HttpRequest request = httpRequestBuilder.uri(URI.create(url)).build();

        try {
            final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            final int statusCode = response.statusCode();
            final String body = response.body();

            if (statusCode == 200) {
                return objectMapper.readValue(body, resourceType);
            } else if (statusCode == 404) {
                if (LOG.isDebugEnabled())
                    LOG.debug("Unable to get resource from url: {}. Status code: {} and message: {}", url, statusCode, body);
            } else if (statusCode == 520) {
                // 520 Origin Error (CloudFlare)
                throw new ConnectIOException("Swapi server unreachable, response body: " + body);
            }

            return null;

        } catch (Exception e) {
            LOG.error("Unable to get resource from url: " + url, e);
            throw new RuntimeException("An exception occurred.");
        }
    }

    private Collection<PersonImpl> getRemainingPages(PersonSearchResultPage firstPage, String url) {

        if (StringUtils.isNotBlank(firstPage.getNext())) {

            final String searchUrlPage = url + "&page=";
            final List<CompletableFuture<PersonSearchResultPage>> pageFutures = new ArrayList<>();

            final double totalPages = Math.ceil((double) firstPage.getCount() / (double) firstPage.getResults().size());

            for (int i = 2; i <= totalPages; i++) {
                int finalI = i;
                pageFutures.add(supplyAsync(() -> fetchResource(searchUrlPage + finalI, PersonSearchResultPage.class)));
            }

            return pageFutures.stream()
                    .map(CompletableFuture::join)
                    .map(PersonSearchResultPage::getResults)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private Class<?> inferType(String swapiUrl) {
        if (swapiUrl.startsWith(config.getSwapiPeopleSearchUrl())) return PersonSearchResultPage.class;
        if (swapiUrl.matches("^(" + config.getSwapiPeopleUrl() + ")\\d+/?$")) return PersonImpl.class;
        if (swapiUrl.matches("^(" + config.getSwapiFilmsUrl() + ")\\d+/?$")) return MovieImpl.class;
        throw new IllegalArgumentException(String.format("Cannot map url \"%s\" to type", swapiUrl));
    }
}
