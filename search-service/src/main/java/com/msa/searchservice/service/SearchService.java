package com.msa.searchservice.service;

import com.msa.searchservice.dto.ProductResponse;
import com.msa.searchservice.model.Product;
import com.msa.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final SearchRepository searchRepository;

    public List<ProductResponse> searchProduct(String text, double minPrice, double maxPrice){
        List<Product> products = this.searchRepository.searchProduct(text, minPrice, maxPrice);

        return products.stream().map(this::mapTpProductResponse).toList();

    }

    private ProductResponse mapTpProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imgSrc(product.getImgSrc())
                .merchantId(product.getMerchantId())
                .categoryName(product.getCategoryName())
                .isShippedForFree(product.isShippedForFree())
                .build();

    }

}
