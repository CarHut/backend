package com.carhut.proxy.model;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestModel {
    HttpServletRequest getRequest();
    String getBody();
    String getBearerToken();
    String getContentType();
}
