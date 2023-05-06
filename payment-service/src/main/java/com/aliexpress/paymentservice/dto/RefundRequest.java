package com.aliexpress.paymentservice.dto;

public class RefundRequest {
    private String chargeId;

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}
