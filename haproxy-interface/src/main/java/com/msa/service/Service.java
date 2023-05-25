package com.msa.service;

import java.util.HashMap;
import java.util.Objects;

public class Service {
    private String appName;
    private String instanceId;
    private String port;
    private String host;

    public Service(String appName, String instanceId, String host, String port) {
        this.instanceId = instanceId;
        this.appName = appName;
        this.port = port;
        this.host = host;
    }

    public String toString() {
        return "ServiceInstance{" +
                "instanceId='" + instanceId + '\'' +
                ", appName='" + appName + '\'' +
                ", port='" + port + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return appName.equals(service.appName) && instanceId.equals(service.instanceId) && port.equals(service.port) && host.equals(service.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, instanceId, port, host);
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("appName", this.appName);
        map.put("instanceId", this.instanceId);
        map.put("port", this.port);
        map.put("host", this.host);
        return map;
    }

}
