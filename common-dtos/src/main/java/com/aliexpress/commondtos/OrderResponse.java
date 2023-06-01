package com.aliexpress.commondtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderResponse{
    private String id;
    private String user_id;
    private String status;
    private double total_price;
    private double shipping;
    private Date date;
    private List<ItemDTO> items;
}

