package com.aliexpress.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String id;
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

