package com.carhutchat.services;

import com.carhutchat.paths.CarHutAPIEndpoints;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service
public class ExternalAPIService {

    public String getUserIdByName(String recipient) throws IOException {
        URL url = URI.create(CarHutAPIEndpoints.GET_USER_ID_BY_USERNAME_BASE_PATH + recipient).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestMethod("GET");
        InputStream response = connection.getInputStream();
        StringBuilder sb = new StringBuilder();
        for (byte ch : response.readAllBytes()) {
            sb.append(Character.toString(ch));
        }

        return sb.toString();
    }

}
