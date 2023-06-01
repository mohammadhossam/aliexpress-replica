package com.aliexpress.orderservice.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Document("orders")
@Sharded(shardKey = { "user_id" })
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order implements Serializable{
    @Id
    private String id;
    private String user_id;
    private String status;
    private double total_price;
    private double shipping;
    private Date date;
    private List<Item> items;
}