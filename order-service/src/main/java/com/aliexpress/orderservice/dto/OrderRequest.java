package com.aliexpress.orderservice.dto;

import com.aliexpress.orderservice.models.Item;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {
    private String user_id;
    private List<ItemDTO> items;
}
