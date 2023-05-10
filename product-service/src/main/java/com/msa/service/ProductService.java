package com.msa.service;

import com.msa.dto.CreateProductRequest;
import com.msa.dto.ProductResponse;
import com.msa.model.Product;
import com.msa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPrice())
                .imgSrc(createProductRequest.getImgSrc())
                .merchantId(createProductRequest.getMerchantId())
                .categoryName(createProductRequest.getCategoryName())
                .isShippedForFree(createProductRequest.isShippedForFree())
                .build();

        Product createdProduct = productRepository.save(product);

        return mapFromProductToProductResponse(createdProduct);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapFromProductToProductResponse)
                .toList();
    }

    private ProductResponse mapFromProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description((product.getDescription()))
                .price(product.getPrice())
                .imgSrc(product.getImgSrc())
                .merchantId(product.getMerchantId())
                .categoryName(product.getCategoryName())
                .isShippedForFree(product.isShippedForFree())
                .build();
    }
}
