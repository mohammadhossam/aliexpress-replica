package com.example.mongodbdemo.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
public class Order {
    @Id
    private UUID id;
    private UUID user_id;
    private String status;
    private double total_price;
    private Date date;
    private List<Item> items;

    public Order(UUID user_id, String status, double total_price, List<Item> items) {
        super();
        this.id = UUID.randomUUID();
        this.user_id = user_id;
        this.status = status;
        this.total_price = total_price;
        this.date = new Date();
        this.items = items;
    }
}