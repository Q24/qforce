package nl.qnh.qforce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.qnh.qforce.domain.PersonImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl service;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpResponse<Object> response;

    @Captor
    private ArgumentCaptor<HttpRequest> httpRequestCaptor;

    @Mock
    private HttpClient httpClient;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(service, "peopleEndpoint", "https://example.com/person/");
        ReflectionTestUtils.setField(service, "httpClient", httpClient);
    }

    @Test
    void shouldFetchPerson() throws Exception {

        when(httpClient.send(httpRequestCaptor.capture(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);

        service.get(1L);

        final List<HttpRequest> captures = httpRequestCaptor.getAllValues();
        assertEquals(1, captures.size());
        assertEquals("https://example.com/person/1", captures.get(0).uri().toString());
    }

    @Test
    void shouldFetchMoviesForPerson() throws Exception {

        final PersonImpl person = new PersonImpl();
        final List<String> movieUrls = Arrays.asList("https://example.com/films/10", "https://example.com/films/11", "https://example.com/films/100");
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
}