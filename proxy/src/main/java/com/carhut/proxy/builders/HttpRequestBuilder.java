package com.carhut.proxy.builders;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class HttpRequestBuilder {

    public static HttpRequest buildRequest(HttpServletRequest request, String payload, String bearerToken, String contentType) throws URISyntaxException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URIBuilder.buildURIRoute(request))
                .header("Authorization", bearerToken)
                .header("Content-Type", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        return httpRequest;
    }

}
