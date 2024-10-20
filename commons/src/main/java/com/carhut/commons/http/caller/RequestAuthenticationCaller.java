package com.carhut.commons.http.caller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class RequestAuthenticationCaller {

    private final String securityOrigin = "http://localhost:8081/security-service";

    public Boolean isRequestAuthenticated(String userId, String bearerToken) throws IOException, InterruptedException {
        HttpRequest request = prepareRequest(userId, bearerToken);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body().equals("true");
        }
        return false;
    }

    public CompletableFuture<HttpResponse<String>> isRequestAuthenticatedAsync(String userId, String bearerToken) {
        HttpRequest request = prepareRequest(userId, bearerToken);
        CompletableFuture<HttpResponse<String>> response = HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private HttpRequest prepareRequest(String userId, String bearerToken) {
        String body = String.format("""
            {
                "userId": "%s",
                "bearerToken": "%s"
            }
            """, userId, bearerToken.substring(7));
        return HttpRequest.newBuilder()
                .uri(URI.create(securityOrigin + "/authenticated"))
                .setHeader("Authorization", bearerToken)
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
