package com.msa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String imgSrc; // TODO: change so that a file is sent and not a URL
    private String merchantId;
    private String categoryName;
    private boolean isShippedForFree;
}
