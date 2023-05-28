package com.aliexpress.orderservice.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private String id;
    private double price;
    private Integer quantity;
    private Long merchantId;
}
