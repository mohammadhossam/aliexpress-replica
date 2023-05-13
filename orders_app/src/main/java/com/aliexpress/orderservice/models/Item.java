package com.aliexpress.orderservice.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private int quantity;
    private double price;
    private Long merchantId;
}