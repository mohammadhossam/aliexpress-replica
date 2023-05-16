package com.service;

public class ServiceInstance {
    private String appName;
    private String port;
    private String host;

    public ServiceInstance(String name, String host, String port) {
        this.appName = name;
        this.port = port;
        this.host = host;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "appName='" + appName + '\'' +
                ", port='" + port + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

}
