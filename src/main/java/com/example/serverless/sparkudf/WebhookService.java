package com.example.serverless.sparkudf; // Updated package

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebhookService {

    public String getPublicIpAddress() {
        String ip = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
//            URL url = new URL("https://api.ipify.org/");
             URL url = new URL("https://apache-http-server");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                ip = response.toString();
            }
            } catch (Exception e) {
            }
        return ip;
    }
}