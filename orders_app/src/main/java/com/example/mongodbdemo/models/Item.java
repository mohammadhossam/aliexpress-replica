package com.example.mongodbdemo.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Item {

    private UUID id;
    private int quantity;
    private double price;

    public Item(
            final UUID id,
            final int quantity,
            final double price
            )

    {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }
}