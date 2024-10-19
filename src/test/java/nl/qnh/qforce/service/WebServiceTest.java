package nl.qnh.qforce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class WebServiceTest {

    @Test
    void testFetchMany() {
        try {
            List<String> urls = new ArrayList<>();
            urls.add("https://swapi.dev/api/people/1/");
            urls.add("https://swapi.dev/api/people/1/");

            List<HttpResponse<String>> responses = WebService.fetchMany(urls);
            List<String> responseBodies = responses.stream().map(response -> response.body()).collect(Collectors.toList());

            System.out.println(responseBodies.get(0));

            assertEquals(urls.size(), responseBodies.size());
            assertTrue(responseBodies.get(0).contains("Luke Skywalker"));
            assertTrue(responseBodies.get(1).contains("Luke Skywalker"));

        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }   
    }

    @Test
    void testFetchOne() {
        try {
            String url = "https://swapi.dev/api/people/1/";

            HttpResponse<String> response = WebService.fetchOne(url);
            String responseBody = response.body();

            assertTrue(responseBody.contains("Luke Skywalker"));
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }   
    }
}
