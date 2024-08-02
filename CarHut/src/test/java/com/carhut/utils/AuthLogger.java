package com.carhut.utils;

import com.carhut.paths.NetworkPaths;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class AuthLogger {

    /**
     *
     * @return bearer token
     */
    public String loginToCarHutAPI() throws InterruptedException, IOException {
        Thread.sleep(1000);
        String username = "admin";
        String password = "admin";

        // Login
        URL url = new URL(NetworkPaths.publicIPAddress + ":8082/api/auth/authenticate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            return null;
        }

        InputStream stream = conn.getInputStream();
        String bearerToken = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            bearerToken = br.readLine();
        }

        Thread.sleep(2000);

        return bearerToken;
    }

}
