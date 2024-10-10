package com.carhut.savedsearchesservice.requests;

public class RemoveSavedSearchRequestModel {
    private String searchId;
    private String userId;

    public RemoveSavedSearchRequestModel() {}

    public RemoveSavedSearchRequestModel(String searchId, String userId) {
        this.searchId = searchId;
        this.userId = userId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
