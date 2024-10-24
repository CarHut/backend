package com.carhut.proxy.model;

import com.fasterxml.jackson.databind.json.JsonMapper;

public final class UnifiedRESTResponseModel implements ResponseModel {

    private String responseBody;
    private Integer statusCode;
    private String message;

    public UnifiedRESTResponseModel() {}

    public UnifiedRESTResponseModel(String responseBody, Integer statusCode, String message) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String toJson() {
        try {
            JsonMapper jsonMapper = new JsonMapper();
            return jsonMapper.writeValueAsString(this);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
