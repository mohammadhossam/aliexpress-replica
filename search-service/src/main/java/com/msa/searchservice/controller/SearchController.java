package com.msa.searchservice.controller;

import com.msa.searchservice.dto.ProductRequest;
import com.msa.searchservice.dto.ProductResponse;
import com.msa.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProduct(@RequestBody HashMap<String, Object> productRequest)
    {
        double minPrice = 0, maxPrice = Double.MAX_VALUE;
        if(productRequest.containsKey("minPrice"))
        {
            if(productRequest.get(("minPrice")) instanceof Integer)
                minPrice = (double) ((int)productRequest.get("minPrice"));
            else
                minPrice = (double) productRequest.get("minPrice");
        }
        if(productRequest.containsKey("maxPrice"))
        {
            if(productRequest.get(("maxPrice")) instanceof Integer)
                maxPrice = (double) ((int)productRequest.get("maxPrice"));
            else
                maxPrice = (double) productRequest.get("maxPrice");
        }

        return this.searchService.searchProduct((String) productRequest.get("text"), minPrice, maxPrice);
    }
}
