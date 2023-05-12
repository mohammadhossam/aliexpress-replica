package com.msa.searchservice.controller;

import com.msa.searchservice.dto.ProductRequest;
import com.msa.searchservice.dto.ProductResponse;
import com.msa.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProduct(@RequestBody ProductRequest productRequest)
    {
        return this.searchService.searchProduct(productRequest.getText());
    }
}
