package com.msa.models;

public enum ServiceType {
    discoverServer("discovery-server"),
    authenticationService("authentication-service"),
    ordersService("orders_app"),
    usersService("users-service"),
    paymentService("payment-service"),
    inventoryService("inventory-service");
    private final String directory;

    ServiceType(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
