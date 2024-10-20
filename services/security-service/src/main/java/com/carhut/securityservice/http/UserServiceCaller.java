package com.carhut.securityservice.http;

import com.carhut.securityservice.security.model.RawUser;
import com.carhut.securityservice.security.serialization.ResponseSerializer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UserServiceCaller {
    private final ResponseSerializer<RawUser> rawUserResponseSerializer;

    public UserServiceCaller() {
        this.rawUserResponseSerializer = new ResponseSerializer<>();
    }

    public RawUser getUserCredentialsWithUsername(String username) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequestForUserCredentialsWithUsernameParam(username);

        if (response.statusCode() != 200) {
            System.out.println("Cannot fetch user credentials.");
            return null;
        }

        return rawUserResponseSerializer.deserialize(response.body(), RawUser.class);
    }

    public RawUser getUserCredentialsWithUserId(String userId) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequestForUserCredentialsWithUserIdParam(userId);

        if (response.statusCode() != 200) {
            System.out.println("Cannot fetch user credentials.");
            return null;
        }

        return rawUserResponseSerializer.deserialize(response.body(), RawUser.class);
    }

    private HttpResponse<String> sendRequestForUserCredentialsWithUsernameParam(String username) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8091/user-service/security-get-user-credentials?username=" + username))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private HttpResponse<String> sendRequestForUserCredentialsWithUserIdParam(String userId) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8091/user-service/security-get-user-credentials?user-id=" + userId))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
