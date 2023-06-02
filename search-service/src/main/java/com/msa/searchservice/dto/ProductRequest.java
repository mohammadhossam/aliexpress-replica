package com.msa.searchservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductRequest {
    private String text;
}
