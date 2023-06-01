package com.aliexpress.orderservice.models;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Item implements Serializable{
    private String id;
    private int quantity;
    private double price;
    private Long merchantId;
}