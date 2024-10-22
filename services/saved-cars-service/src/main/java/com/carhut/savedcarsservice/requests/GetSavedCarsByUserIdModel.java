package com.carhut.savedcarsservice.requests;

public class GetSavedCarsByUserIdModel {
    private String id;

    public GetSavedCarsByUserIdModel() {}

    public GetSavedCarsByUserIdModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
