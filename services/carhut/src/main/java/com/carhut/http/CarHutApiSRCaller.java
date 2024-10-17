package com.carhut.http;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.commons.model.CarHutCar;
import com.carhut.requests.requestmodels.CarHutCarFilterModel;
import com.carhut.requests.requestmodels.RemoveCarRequestModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CarHutApiSRCaller {

    private final String basePath = "http://localhost:8000/carhut-api-sr";

    public CompletableFuture<CarHutCar> getCarWithId(String carId) throws URISyntaxException {
        CompletableFuture<HttpResponse<String>> response = sendRequestToGetCarWithId(carId);
        CompletableFuture<CarHutCar> cfCarHutCar = new CompletableFuture<>();

        response.whenComplete((result, ex) -> {
            if (ex != null) {
                cfCarHutCar.completeExceptionally(ex);
            } else if (result.statusCode() == 200) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    CarHutCar car = objectMapper.readValue(result.body(), CarHutCar.class);
                    cfCarHutCar.complete(car);
                } catch (Exception e) {
                    cfCarHutCar.completeExceptionally(e);
                }
            } else {
                cfCarHutCar.complete(null);
            }
        });

        return cfCarHutCar;
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToGetCarWithId(String carId) throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/get-car-with-id?car-id=" + carId))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> removeOffer(RemoveCarRequestModel removeCarRequestModel)
            throws URISyntaxException {
        return sendRequestToRemoveOffer(removeCarRequestModel.getCarId());
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToRemoveOffer(String carId)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/remove-offer?car-id=" + carId))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> getListingsWithUserId(String userId) throws URISyntaxException {
        return sendRequestToGetListingsWithUserId(userId);
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToGetListingsWithUserId(String userId)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/get-listings?user-id=" + userId))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<List<CarHutCar>> getCarsWithFilter(CarHutCarFilterModel carHutCarFilterModel,
                                                                String sortBy, String sortOrder, Integer offersPerPage,
                                                                Integer currentPage)
            throws URISyntaxException {
        CompletableFuture<HttpResponse<String>> response = sendRequestToGetCarsWithFilter(carHutCarFilterModel, sortBy,
                sortOrder, offersPerPage, currentPage);
        CompletableFuture<List<CarHutCar>> statusCf = new CompletableFuture<>();
        response.whenComplete((result, ex) -> {
            if (ex != null) {
                statusCf.completeExceptionally(ex);
            } else if (result.statusCode() == 200) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<CarHutCar> carListings = objectMapper.readValue(result.body(), new TypeReference<>() {});
                    statusCf.complete(carListings);
                } catch (Exception e) {
                    statusCf.completeExceptionally(e);
                }
            } else {
                statusCf.complete(null);
            }

            statusCf.complete(null);
        });
        return statusCf;
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToGetCarsWithFilter(CarHutCarFilterModel filterModel,
                                                                                   String sortBy, String sortOrder,
                                                                                   Integer offersPerPage,
                                                                                   Integer currentPage)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/get-cars-with-filters?sort-by=" + sortBy + "&sort-order=" + sortOrder
                + "&offers-per-page=" + offersPerPage + "&current-page=" + currentPage))
                .POST(HttpRequest.BodyPublishers.ofString(filterModel.toJson()))
                .setHeader("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<Integer> getNumberOfFilteredCars(CarHutCarFilterModel carHutCarFilterModel) throws URISyntaxException {
        CompletableFuture<HttpResponse<String>> response = sendRequestToGetNumberOfFilteredCars(carHutCarFilterModel);
        CompletableFuture<Integer> statusCf = new CompletableFuture<>();
        response.whenComplete((result, ex) -> {
            if (result.statusCode() == 200) {
                try {
                    statusCf.complete(Integer.parseInt(result.body()));
                } catch (Exception e) {
                    statusCf.complete(null);
                }
            } else {

            }
            statusCf.complete(null);
        });
        return statusCf;
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToGetNumberOfFilteredCars(CarHutCarFilterModel filter)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/get-number-of-filtered-cars"))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(filter.toJson()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> addCarToDatabaseAsync(CarHutCar car)
            throws URISyntaxException {
        return sendRequestToAddCarToDatabase(car);
    }

    public CompletableFuture<HttpResponse<String>> sendRequestToAddCarToDatabase(CarHutCar car)
            throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/add-car"))
                .setHeader("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(car.toJson()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

    }
}
