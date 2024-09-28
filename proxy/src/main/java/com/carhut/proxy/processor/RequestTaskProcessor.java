package com.carhut.proxy.processor;

import com.carhut.proxy.builder.URLBuilder;
import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import com.carhut.proxy.model.RequestModel;
import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTResponseModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RequestTaskProcessor implements TaskProcessor<RequestModel, CompletableFuture<ResponseModel>> {

    public RequestTaskProcessor() {}

    @Override
    public CompletableFuture<ResponseModel> processRequest(RequestModel request) {
        Function<Void, CompletableFuture<ResponseModel>> function = buildFunctionToExecute(request);
        return WorkerThreadDispatcher.getInstance().dispatchThread(function);
    }

    @Override
    public Function<Void, CompletableFuture<ResponseModel>> buildFunctionToExecute(RequestModel input) {
        return (unused) -> {
            HttpRequest httpRequest = buildRequest(input);

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        // Build a ResponseModel from the HttpResponse
                        int statusCode = httpResponse.statusCode();
                        String responseBody = httpResponse.body();

                        if (statusCode >= 200 && statusCode < 300) {
                            return (ResponseModel) new UnifiedRESTResponseModel(responseBody, statusCode, "Success");
                        } else {
                            return new UnifiedRESTResponseModel(responseBody, statusCode, "Error");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return new UnifiedRESTResponseModel("Failed to complete request", 500, "Failure");
                    });
        };
    }

    private HttpRequest buildRequest(RequestModel request) {
        try {
            URI uri = URLBuilder.buildRequestUrl(request.getRequest());

            HttpRequest.Builder httpRequest = HttpRequest.newBuilder();
            httpRequest.uri(uri);
            if (request.getBearerToken() != null) {
                httpRequest.header("Authorization", request.getBearerToken());
            }

            if (request.getContentType() != null) {
                httpRequest.header("Content-Type", request.getContentType());
            } else {
                httpRequest.header("Content-Type", "application/json");
            }

            switch (request.getRequest().getMethod()) {
                case "GET":
                    httpRequest.GET();
                    break;
                case "POST":
                    if (request.getBody() == null || request.getBody().isBlank()) {
                        return null;
                    }
                    httpRequest.POST(HttpRequest.BodyPublishers.ofString(request.getBody()));
                    break;
                default:
                    return null;
            }

            httpRequest.timeout(Duration.ofSeconds(20));

            return httpRequest.build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
