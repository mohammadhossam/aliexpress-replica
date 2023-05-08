package com.aliexpress.orderservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Document("orders")
@Sharded(shardKey = { "user_id" })
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    private String user_id;
    private String status;
    private double total_price;
    private Date date;
    private List<Item> items;
}