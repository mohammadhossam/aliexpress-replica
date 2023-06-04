package com.msa.models;

public enum ServiceType {
    buyerAuthenticationService("buyer-authentication-service"),
    //    inventoryService("inventory-service"),
    merchantAuthenticationService("merchant-authentication-service"),
//    orderService("order-service"),
//    paymentService("payment-service"),
//    productService("product-service"),
//    searchService("search-service"),
//    usersService("users-service")
    ;
    private final String directory;

    ServiceType(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public String toString() {
        return getDirectory();
    }

}
