package com.carhut.savedcarsservice.http.caller;

import com.carhut.commons.model.CarHutCar;
import com.carhut.savedcarsservice.models.SavedCarByUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CarHutApiCaller {

    private final String basePath = "http://localhost:8001/carhut-api";

    public CompletableFuture<HttpResponse<String>> getCarsByCarIds(List<SavedCarByUser> savedCarsByUser, String userId,
                                                                   String bearerToken)
            throws URISyntaxException {
        return sendRequestToGetCarsByCarIds(savedCarsByUser, userId, bearerToken);
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToGetCarsByCarIds(List<SavedCarByUser> savedCarsByUser,
                                                                                 String userId,
                                                                                 String bearerToken)
            throws URISyntaxException {
        String body = prepareBody(savedCarsByUser);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/get-cars-by-car-ids?user-id=" + userId))
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private String prepareBody(List<SavedCarByUser> savedCarsByUser) {
        StringBuilder body = new StringBuilder();
        body.append("[");
        for (int i = 0; i < savedCarsByUser.size(); i++) {
            if (i == savedCarsByUser.size() - 1) {
                body.append("\"").append(savedCarsByUser.get(i).getCarId()).append("\"");
            } else {
                body.append("\"").append(savedCarsByUser.get(i).getCarId()).append("\"").append(",");
            }
        }
        body.append("]");
        return body.toString();
    }

}
