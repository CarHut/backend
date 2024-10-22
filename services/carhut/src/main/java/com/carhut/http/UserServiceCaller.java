package com.carhut.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserServiceCaller {

    private final String baseAddress = "http://localhost:8091/user-service";

    public UserServiceCaller() {}

    public CompletableFuture<HttpResponse<String>> updateOffersCountForUserWithId(String userId, String bearerToken)
            throws URISyntaxException {
        return sendRequestToUpdateOffersCountForUserWithId(userId, bearerToken);
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToUpdateOffersCountForUserWithId(String userId,
                                                                                                String bearerToken)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI( baseAddress + "/update-car-count-for-user-id?user-id=" + userId))
                .setHeader("Authorization", bearerToken)
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
