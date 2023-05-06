package com.example.mongodbdemo.dtos;

public class PaymentRequest {
    private String customerId;
    private double amount;

    // getters and setters omitted for brevity

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
