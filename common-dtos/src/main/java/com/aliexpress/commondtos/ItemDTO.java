package com.aliexpress.commondtos;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    private String id;
    private double price;
    private Integer quantity;
    private Long merchantId;
}
