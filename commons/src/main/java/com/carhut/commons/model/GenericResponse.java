package com.carhut.commons.model;

public class GenericResponse<T> {
     private String status;
     private int code;
     private String message;
     private T body;

    public GenericResponse(String status, int code, String message, T body) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public GenericResponse() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
