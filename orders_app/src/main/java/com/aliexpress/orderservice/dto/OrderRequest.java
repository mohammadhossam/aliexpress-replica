package com.aliexpress.orderservice.dto;

import com.aliexpress.orderservice.models.Item;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String user_id;
    private String status;
    private double total_price;
    private Date date;
    private List<ItemDTO> items;

    @Override
    public String toString(){
        return "OrderRequest \n user_id: "+user_id;
    }
}
