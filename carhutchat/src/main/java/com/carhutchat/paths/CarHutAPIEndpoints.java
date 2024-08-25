package com.carhutchat.paths;

import com.carhutchat.secret.SecretPaths;

public class CarHutAPIEndpoints {

    public final SecretPaths paths = new SecretPaths();
    private final String BASE_PATH = paths.getBASE_CARHUT_IP_ADDRESS();
    private final String GET_USER_ID_BY_USERNAME_BASE_PATH = paths.getBASE_CARHUT_IP_ADDRESS() + "/getUserIdByUsername?username="; // + ENTER_USERNAME
    private final String CORS_URL = new SecretPaths().getCORS_URL();

    public String getBASE_PATH() {
        return BASE_PATH;
    }

    public String getGET_USER_ID_BY_USERNAME_BASE_PATH() {
        return GET_USER_ID_BY_USERNAME_BASE_PATH;
    }

    public String getCORS_URL() {
        return CORS_URL;
    }
}
