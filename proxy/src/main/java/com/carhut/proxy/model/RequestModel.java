package com.carhut.proxy.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestModel {
    HttpServletRequest getRequest();
    String getBody();
    String getBearerToken();
    String getContentType();
    List<MultipartFile> getMultipartFiles();
    String getMultipartJson();
}
