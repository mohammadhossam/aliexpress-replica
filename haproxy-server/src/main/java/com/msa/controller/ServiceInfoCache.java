package com.msa.controller;

import java.util.ArrayList;
import java.util.List;

public class ServiceInfoCache<T> {

    List<T> serviceCache;

    public ServiceInfoCache() {
        this.serviceCache = new ArrayList<>();
    }

    public void add(T service) {
        serviceCache.add(service);
    }

    public boolean isServiceAvailable(T service) {
        return serviceCache.contains(service);
    }
}
