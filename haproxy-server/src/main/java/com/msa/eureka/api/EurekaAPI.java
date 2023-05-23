package com.msa.eureka.api;

import com.msa.controller.Properties;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class EurekaAPI {

    private static String getAllInstancesReq = "eureka/v2/apps";

    public EurekaAPI() {

    }

    public String getAllInstances() throws IOException {
        String endpoint = Properties.getProperties().getProperty("eurekaServerHost");
        if (endpoint == null)
            throw new IOException("Couldn't find the eurekaServerHost property");

        // establishing the connection and adding headers
        URL eurekaURL = new URL(endpoint + getAllInstancesReq);
        HttpURLConnection httpConn = (HttpURLConnection) eurekaURL.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("Accept", "application/json");

        int responseCode = httpConn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return null;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        httpConn.disconnect();

        return response.toString();
    }

}
