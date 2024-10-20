package com.carhut.proxy.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public final class UnifiedRESTRequestModel implements RequestModel {

    private final HttpServletRequest request;
    private final String body;
    private final String bearerToken;
    private final String contentType;
    private final String json;
    private final List<MultipartFile> files;

    public UnifiedRESTRequestModel(HttpServletRequest request, String body, String bearerToken, String contentType,
                                   List<MultipartFile> files, String json) {
        this.request = request;
        this.body = body;
        this.bearerToken = bearerToken;
        this.contentType = contentType;
        this.files = files;
        this.json = json;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getBody() {
        return body;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public List<MultipartFile> getMultipartFiles() {
        return files;
    }

    @Override
    public String getMultipartJson() {
        return json;
    }


}
