package com.carhut.proxy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@JsonSerialize
public final class UnifiedRESTRequestModel implements RequestModel {

    private HttpServletRequest request;
    private String body;
    private String bearerToken;
    private String contentType;
    private String json;
    private List<MultipartFile> files;

    public UnifiedRESTRequestModel(HttpServletRequest request, String body, String bearerToken, String contentType,
                                   List<MultipartFile> files, String json) {
        this.request = request;
        this.body = body;
        this.bearerToken = bearerToken;
        this.contentType = contentType;
        this.files = files;
        this.json = json;
    }

    public UnifiedRESTRequestModel() {}

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

    public String toCacheJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\t\"requestQueryString\": \"").append(request.getRequestURI()).append("\",\n");
        sb.append("\t\"body\": \"").append(body).append("\",\n");
        sb.append("\t\"contentType\": \"").append(contentType).append("\",\n");
        sb.append("\t\"json\": \"").append(json).append("\"\n");
        sb.append("}");
        return sb.toString();
    }

}
