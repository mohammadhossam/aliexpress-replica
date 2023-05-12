package com.aliexpress.paymentservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {
    public enum Currency {
        USD;
    }
    private double amount;
    private String stripeToken;
}
