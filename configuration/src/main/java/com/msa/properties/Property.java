package com.msa.properties;

import org.apache.commons.text.*;

import java.util.HashMap;

public class Property {
    private String propertyName; // spring.data.redis.port
    private String val; // should contain placeholders for the values of external resources for example: {users-service-cache.port}

    public Property(String propertyName, String val, HashMap<String, String> placeHolders) {
        this.propertyName = propertyName;
        this.val = val;
    }

    public String toString() {
        return "--" + propertyName + "=" + getVal();
    }

    private String getVal() {
        return val;
    }
}
