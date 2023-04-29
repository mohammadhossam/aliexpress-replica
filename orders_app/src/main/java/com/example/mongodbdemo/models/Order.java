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
@Sharded(shardKey = { "userId" })
public class Order {
    @Id
    private UUID id;
    private UUID userI_id;
    private String status;
    private double total_price;
    private Date date;
    private List<Item> items;

    public Order(UUID id, UUID userI_id, String status, double total_price,  Date date,  List<Item> items) {
        super();
        this.id = id;
        this.userI_id = userI_id;
        this.status = status;
        this.total_price = total_price;
        this.date = date;
        this.items = items;
    }
}