package com.carhut.proxy.model;

public interface ResponseModel {
    String getResponseBody();
    Integer getStatusCode();
    String getMessage();
    String toJson();
}
