package com.aliexpress.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponse {
    private String id;
    private String user_id;
    private String status;
    private double total_price;
    private Date date;
    private List<ItemDTO> items;


}

