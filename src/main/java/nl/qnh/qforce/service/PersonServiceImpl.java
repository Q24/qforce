package nl.qnh.qforce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.qnh.qforce.domain.Movie;
import nl.qnh.qforce.domain.MovieImpl;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.PersonImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.rmi.ConnectIOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The person service implementation to search and retrieve persons.
 * <br/><br/>
 * Use javas immutable {@link HttpClient} to execute the http requests, which are simple GET requests and only need the
 * <i>Accept</i> header to receive the correct data format (JSON). Subsequently, the Jackson {@link ObjectMapper} is leveraged
 * to deserialize the JSON data into a populated {@link PersonImpl}.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

    private HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    private HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder().setHeader("Accept", "application/json");

    @Value("${qforce.swapi.people.url}")
    private String peopleEndpoint;

    private ObjectMapper objectMapper;

    public PersonServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Person> search(String query) {
        // TODO: Implement
        LOG.debug("Search request for string: " + query);
        return null;
    }

    @Override
    public Optional<Person> get(long id) {

        final URI url = URI.create(peopleEndpoint + id);
        final PersonImpl person = getResource(url, PersonImpl.class);

        if (person == null) return Optional.empty();

        final List<Movie> movies = getMovies(person.getMovieUrls());
        person.setMovies(movies);

        return Optional.of(person);
    }

    private List<Movie> getMovies(List<String> urls) {
        return urls.stream()
                .map(uri -> getResource(URI.create(uri), MovieImpl.class))
                .collect(Collectors.toList());
    }

    private <T> T getResource(URI url, Class<T> resourceType) {

        final HttpRequest request = httpRequestBuilder.uri(url).build();

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
}
