package com.msa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imgSrc;
    private String merchantId;
    private String categoryName;
    private boolean isShippedForFree;
}
