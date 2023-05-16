package com.eureka.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class EurekaAPI {

    private static String getAllInstancesReq = "eureka/v2/apps";
    private static Properties properties = new Properties();

    public EurekaAPI() throws IOException {
        loadPropertiesFile();
    }


    private void loadPropertiesFile() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("application.properties");
        assert is != null;
        properties.load(is);
    }


    public String getAllInstances() throws IOException {
        String endpoint = properties.getProperty("eurekaServerHost");
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

    public static void main(String[] args) throws IOException {
        System.out.println(new EurekaAPI().getAllInstances());
    }
}
