package nl.qnh.qforce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import nl.qnh.qforce.domain.Analytic;
import nl.qnh.qforce.repositories.AnalyticRepository;

public class AnalyticServiceTest {

    @InjectMocks
    private AnalyticService analyticService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AnalyticRepository analyticRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAnalytic() {
        
        // Create fake data and functions
        String expectedServletPath = "/persons";
        String expectedQueryString = "q=r2";
        String expectedIpAddress = "192.168.1.1";
        String expectedUserAgent = "Mozilla/5.0";
        HttpStatus expectedStatusCode = HttpStatus.OK;
        when(request.getHeader("User-Agent")).thenReturn(expectedUserAgent);
        when(request.getHeader("X-Forwarded-For")).thenReturn(expectedIpAddress);
        when(request.getContextPath()).thenReturn("");
        when(request.getServletPath()).thenReturn(expectedServletPath);
        when(request.getQueryString()).thenReturn(expectedQueryString);

        // Call the function
        Analytic createdAnalytic = analyticService.createAnalytic(request, expectedStatusCode);

        // Get the saved model from the database
        ArgumentCaptor<Analytic> analyticCaptor = ArgumentCaptor.forClass(Analytic.class);
        verify(analyticRepository).save(analyticCaptor.capture());
        Analytic savedAnalytic = analyticCaptor.getValue();

        // Check the results
        assertEquals(expectedServletPath+"?"+expectedQueryString, savedAnalytic.getUrlPath());
        assertEquals(expectedIpAddress, savedAnalytic.getIpAddress());
        assertEquals(expectedUserAgent, savedAnalytic.getUserAgent());
        assertEquals(expectedStatusCode.value(), savedAnalytic.getStatusCode());
        assertNotNull(savedAnalytic.getDateTime());
        assertEquals(createdAnalytic, savedAnalytic);
    }

}
