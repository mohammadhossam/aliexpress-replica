package com.aliexpress.paymentservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayoutRequest {
    public enum Currency {
        USD;
    }
    private String destinationCustomerId;
    private double amount;
}
