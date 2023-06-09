package com.msa.service;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.CommandEnum;
import com.msa.dto.CreateProductRequest;
import com.msa.dto.ProductResponse;
import com.msa.dto.UpdateProductRequest;
import com.msa.model.Product;
import com.msa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final MessageService messageService;

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

        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", createdProduct.getId());
        payload.put("quantity", createProductRequest.getInitialStock());
        Message message = new Message(CommandEnum.CreateInventoryCommand, payload, "ProductService", "product-service",
                "product");
        messageService.publishMessage(message);

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

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProduct(String id) {
        return productRepository.findById(id)
                .map(this::mapFromProductToProductResponse)
                .orElse(null);
    }

    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(String id, UpdateProductRequest updateProductRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updateProductRequest.getName());
                    product.setDescription(updateProductRequest.getDescription());
                    product.setPrice(updateProductRequest.getPrice());
                    product.setImgSrc(updateProductRequest.getImgSrc());
                    product.setMerchantId(updateProductRequest.getMerchantId());
                    product.setCategoryName(updateProductRequest.getCategoryName());
                    product.setShippedForFree(updateProductRequest.isShippedForFree());

                    Product updatedProduct = productRepository.save(product);
                    return mapFromProductToProductResponse(updatedProduct);
                })
                .orElse(null);
    }

    @CacheEvict(value = "products", key = "#id")
    public ProductResponse deleteProduct(String id) {
        ProductResponse deletedProdcut = productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return mapFromProductToProductResponse(product);
                })
                .orElse(null);

        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", deletedProdcut.getId());
        Message message = new Message(CommandEnum.DeleteInventoryCommand, payload, "ProductService", "product-service",
                "product");
        messageService.publishMessage(message);

        return deletedProdcut;
    }
}
