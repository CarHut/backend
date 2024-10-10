package com.carhut.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UserServiceCaller {

    public UserServiceCaller() {}

    public Boolean updateOffersCountForUserWithId(String userId, String bearerToken)
            throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequestToUpdateOffersCountForUserWithId(userId, bearerToken);

        if (response.statusCode() != 200) {
            System.out.println("Cannot fetch user credentials.");
            return null;
        }

        return response.body().equals("true");
    }

    private HttpResponse<String> sendRequestToUpdateOffersCountForUserWithId(String userId, String bearerToken)
            throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8001/user-service/update-car-count-for-user-id?user-id=" + userId))
                .setHeader("Authorization", bearerToken)
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
