package com.msa.controller;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

        private static final java.util.Properties properties = new java.util.Properties();

    static {
        try {
            loadPropertiesFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static java.util.Properties getProperties() throws IOException {
        return properties;
    }

    private static void loadPropertiesFile() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("application.properties");
        assert is != null;
        properties.load(is);
    }

}
