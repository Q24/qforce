package nl.qnh.qforce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.qnh.qforce.config.QforceConfig;
import nl.qnh.qforce.domain.MovieImpl;
import nl.qnh.qforce.domain.PersonImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PersonServiceImplTest {

    private static final String API_BASE_PERSON = "https://example.com/person/";
    private static final String API_BASE_FILMS = "https://example.com/films/";
    private static final String API_BASE_PERSON_SEARCH = "https://example.com/person/?search=";

    @InjectMocks
    private PersonServiceImpl service;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private QforceConfig qforceConfig;

    @Mock
    private HttpResponse<Object> response;

    @Captor
    private ArgumentCaptor<HttpRequest> httpRequestCaptor;

    @Mock
    private HttpClient httpClient;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(service, "httpClient", httpClient);

        when(qforceConfig.getSwapiPeopleUrl()).thenReturn(API_BASE_PERSON);
        when(qforceConfig.getSwapiPeopleSearchUrl()).thenReturn(API_BASE_PERSON_SEARCH);
        when(qforceConfig.getSwapiFilmsUrl()).thenReturn(API_BASE_FILMS);
    }

    @Test
    void shouldFetchPerson() throws Exception {

        when(httpClient.send(httpRequestCaptor.capture(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);

        final long id = 1L;
        service.get(id);

        final List<HttpRequest> captures = httpRequestCaptor.getAllValues();
        assertEquals(1, captures.size());
        assertEquals(API_BASE_PERSON + id, captures.get(0).uri().toString());
    }

    @Test
    void shouldFetchMoviesForPerson() throws Exception {

        final PersonImpl person = new PersonImpl();
        final List<String> movieUrls = Arrays.asList(API_BASE_FILMS + 10, API_BASE_FILMS + 11, API_BASE_FILMS + 100);
        person.setMovieUrls(movieUrls);

        when(httpClient.send(httpRequestCaptor.capture(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("body");
        when(objectMapper.readValue(any(String.class), eq(PersonImpl.class))).thenReturn(person);

        service.get(1L);

        final List<HttpRequest> captures = httpRequestCaptor.getAllValues();
        assertEquals(4, captures.size()); // 1x person plus 3x movie

        final List<String> capturedUris = captures.stream()
                .map(HttpRequest::uri)
                .map(URI::toString)
                .collect(Collectors.toList());

        assertTrue(capturedUris.containsAll(movieUrls));
    }

    @Test
    void shouldInferReturnTypeByUrl() throws Exception {

        final PersonImpl person = new PersonImpl();
        final List<String> movieUrls = Collections.singletonList(API_BASE_FILMS + 10);
        person.setMovieUrls(movieUrls);

        when(httpClient.send(httpRequestCaptor.capture(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("body");
        when(objectMapper.readValue(any(String.class), eq(PersonImpl.class))).thenReturn(person);

        service.get(1L);

        final InOrder inOrder = inOrder(objectMapper);

        inOrder.verify(objectMapper).readValue(anyString(), eq(PersonImpl.class));
        inOrder.verify(objectMapper).readValue(anyString(), eq(MovieImpl.class));

        final List<HttpRequest> captures = httpRequestCaptor.getAllValues();
        assertEquals(2, captures.size()); // 1x person plus 1x movie
    }
}