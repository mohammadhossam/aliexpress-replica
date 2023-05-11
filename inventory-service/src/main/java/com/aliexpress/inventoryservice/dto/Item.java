package com.aliexpress.inventoryservice.dto;;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private Integer quantity;
}