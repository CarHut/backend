package com.carhutchat.services;

import com.carhutchat.paths.CarHutAPIEndpoints;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotOpenHttpConnectionException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotReadResponseStreamException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotSetConnectionPropertyException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ExternalAPIService {

    private final CarHutAPIEndpoints endpoints = new CarHutAPIEndpoints();

    public String getUserIdByName(String recipient) throws ExternalAPIServiceCannotOpenHttpConnectionException, ExternalAPIServiceCannotSetConnectionPropertyException, ExternalAPIServiceCannotReadResponseStreamException {

        HttpURLConnection connection = null;
        try {
            URL url = URI.create(endpoints.getGET_USER_ID_BY_USERNAME_BASE_PATH() + URLEncoder.encode(recipient, StandardCharsets.UTF_8)).toURL();
            connection = (HttpURLConnection) url.openConnection();
        }
        catch (Exception e) {
            throw new ExternalAPIServiceCannotOpenHttpConnectionException("[ExternalAPIService][ERROR] - Cannot open http connection to url: " + endpoints.getGET_USER_ID_BY_USERNAME_BASE_PATH() + ". Stack trace error: " + e.getMessage());
        }

        try {
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
        }
        catch (Exception e) {
            throw new ExternalAPIServiceCannotSetConnectionPropertyException("[ExternalAPIService][ERROR] - Cannot set connection property. Stack trace error: " + e.getMessage());
        }

        StringBuilder sb = new StringBuilder();
        try {
            InputStream response = connection.getInputStream();
            for (byte ch : response.readAllBytes()) {
                sb.append(Character.toString(ch));
            }
        }
        catch (Exception e) {
            throw new ExternalAPIServiceCannotReadResponseStreamException("[ExternalAPIService][ERROR] - Cannot read response. Stack trace error: " + e.getMessage());
        }

        return sb.toString();
    }
}
