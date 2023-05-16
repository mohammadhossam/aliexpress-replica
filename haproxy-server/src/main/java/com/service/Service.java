package com.service;

import java.util.List;

public class Service {
    private String serviceName;
    private List<ServiceInstance> instances;

    public Service(String serviceName, List<ServiceInstance> instances) {
        this.serviceName = serviceName;
        this.instances = instances;
    }

    public List<ServiceInstance> getServiceInstances() {
        return instances;
    }

    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", instances=" + instances +
                '}';
    }

}
