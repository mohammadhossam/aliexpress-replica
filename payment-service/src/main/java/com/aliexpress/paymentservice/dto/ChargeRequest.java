package com.aliexpress.paymentservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeRequest {
    public enum Currency {
        USD;
    }
    private String customerId;
    private double amount;
}
