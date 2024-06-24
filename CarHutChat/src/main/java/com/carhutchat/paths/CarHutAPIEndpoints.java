package com.carhutchat.paths;

import com.carhutchat.secret.SecretPaths;

public class CarHutAPIEndpoints {

    public final static String BASE_PATH = SecretPaths.BASE_CARHUT_IP_ADDRESS;
    public final static String GET_USER_ID_BY_USERNAME_BASE_PATH = BASE_PATH + "/getUserIdByUsername?username="; // + ENTER_USERNAME
    public final static String CORS_URL = SecretPaths.CORS_URL;

}
