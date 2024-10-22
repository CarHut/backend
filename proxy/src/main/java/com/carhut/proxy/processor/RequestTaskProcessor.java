package com.carhut.proxy.processor;

import com.carhut.proxy.builder.URLBuilder;
import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import com.carhut.proxy.model.RequestModel;
import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTResponseModel;
import com.carhut.proxy.util.logger.ProxyLogger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RequestTaskProcessor implements TaskProcessor<RequestModel, CompletableFuture<ResponseModel>> {

    public static final ProxyLogger logger = ProxyLogger.getInstance();

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

            switch (request.getRequest().getMethod()) {
                case "GET":
                    httpRequest.GET();
                    break;
                case "POST":
                    if (request.getMultipartFiles() != null && request.getMultipartJson() != null) {
                        String boundary = "-------------------------WebKitFormBoundary7MA4YWxkTrZu0gW";
                        httpRequest.header("Content-Type", "multipart/form-data; boundary=" + boundary);
                        httpRequest.POST(buildMultipartBody(request, boundary));
                    } else if (request.getBody() != null && !request.getBody().isBlank()) {
                        httpRequest.header("Content-Type",
                                request.getContentType() == null ? "application/json" : request.getContentType());
                        httpRequest.POST(HttpRequest.BodyPublishers.ofString(request.getBody()));
                    } else {
                        return null;
                    }
                    break;
                default:
                    return null;
            }

            httpRequest.timeout(Duration.ofSeconds(20));

            return httpRequest.build();

        } catch (Exception e) {
            logger.logWarn("Exception occurred while trying to build request. Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.BodyPublisher buildMultipartBody(RequestModel request, String boundary) {
        try {
            StringBuilder multipartBody = new StringBuilder();
            multipartBody.append("--").append(boundary).append("\r\n");
            multipartBody.append("Content-Disposition: form-data; name=\"json\"\r\n");
            multipartBody.append("Content-Type: application/json\r\n\r\n");
            multipartBody.append(request.getMultipartJson()).append("\r\n");
            List<MultipartFile> files = request.getMultipartFiles();
            if (files != null) {
                for (MultipartFile file : files) {
                    multipartBody.append("--").append(boundary).append("\r\n");
                    multipartBody.append("Content-Disposition: form-data; name=\"files\"; filename=\"")
                            .append(file.getOriginalFilename()).append("\"\r\n");
                    multipartBody.append("Content-Type: ").append(file.getContentType()).append("\r\n\r\n");
                    multipartBody.append(new String(file.getBytes(), StandardCharsets.UTF_8)).append("\r\n");
                }
            }

            multipartBody.append("--").append(boundary).append("--").append("\r\n");
            return HttpRequest.BodyPublishers.ofString(multipartBody.toString());

        } catch (IOException e) {
            logger.logWarn("Exception occurred while trying to build request. Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
