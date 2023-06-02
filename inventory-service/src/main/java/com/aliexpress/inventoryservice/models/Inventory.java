package com.aliexpress.inventoryservice.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    private String id;
    private Integer quantity;
}