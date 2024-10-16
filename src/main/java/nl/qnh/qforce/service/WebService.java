package nl.qnh.qforce.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class WebService {
    
    /**
     * Function that fetches the response from one GET endpoint
     * @param url
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static HttpResponse<String> fetchOne(String url) throws InterruptedException, ExecutionException {
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        
        HttpResponse<String> response = futureResponse.get();
        return response;
    }

    /**
     * Function that fetches the responses from multipe GET endpoints
     * @param urls
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static List<HttpResponse<String>> fetchMany(List<String> urls) throws InterruptedException, ExecutionException {

        List<CompletableFuture<HttpResponse<String>>> futures = urls.stream()
        .map(url -> {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            return futureResponse;
        }).collect(Collectors.toList());
        
        CompletableFuture<List<HttpResponse<String>>> futureResponses = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList()));
        
        List<HttpResponse<String>> responses = futureResponses.get();
        return responses;
    }
}
