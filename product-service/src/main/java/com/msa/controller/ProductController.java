package com.msa.controller;

import com.msa.dto.CreateProductRequest;
import com.msa.dto.ProductResponse;
import com.msa.dto.UpdateProductRequest;
import com.msa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        ProductResponse productResponse = productService.getProduct(id);

        if (productResponse != null)
            return ResponseEntity.ok(productResponse);

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id, @RequestBody UpdateProductRequest updateProductRequest) {
        ProductResponse productResponse = productService.updateProduct(id, updateProductRequest);

        if (productResponse != null)
            return ResponseEntity.ok(productResponse);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody CreateProductRequest createProductRequest) {
        return productService.createProduct(createProductRequest);
    }

    @PostMapping("/upload-image")
    public String handleFileUpload(@RequestParam("img") MultipartFile img) {
        // TODO: upload to s3
        return "https://google.com";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable String id) {
        ProductResponse productResponse = productService.deleteProduct(id);

        if (productResponse != null)
            return ResponseEntity.ok(productResponse);

        return ResponseEntity.notFound().build();
    }
}
