package com.msa.configManager.properties;

import java.util.ArrayList;

public class ServiceProperties {
    private String applicationName;
    private ArrayList<Property> properties;

    public ServiceProperties(String applicationName, ArrayList<Property> properties) {
        this.applicationName = applicationName;
        this.properties = properties;
    }

    public ArrayList<Property> getProperties() {
        return properties;
}
    public String getServiceName() {
        return applicationName;
    }

    @Override
    public String toString() {
        return "ServiceProperties{" +
                "applicationName='" + applicationName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
