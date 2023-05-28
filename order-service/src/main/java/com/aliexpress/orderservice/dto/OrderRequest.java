package com.aliexpress.orderservice.dto;

import com.aliexpress.commondtos.ItemDTO;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {
    private String user_id;
    private List<ItemDTO> items;
}
