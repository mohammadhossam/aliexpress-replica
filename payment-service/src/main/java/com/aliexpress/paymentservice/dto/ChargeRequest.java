package com.aliexpress.paymentservice.dto;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest {

    public enum Currency {
        USD;
    }
    private double amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}