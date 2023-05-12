package com.aliexpress.paymentservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    //todo- ADD MERCHANT ID

    private String id;
    private double price;
    private Integer quantity;
}
