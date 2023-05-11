package com.aliexpress.inventoryservice.controllers;

import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.dto.StockUpdateRequest;
import com.aliexpress.inventoryservice.models.Inventory;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    Logger logger= LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Inventory createProduct(@RequestBody InventoryRequest request) {
        return inventoryService.createProduct(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int getProductData(@PathVariable String id) {
        return inventoryService.getProductData(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory updateProduct(@PathVariable String id, @RequestBody InventoryRequest request) {
        return inventoryService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable String id) {
        inventoryService.deleteProduct(id);
    }

    @PostMapping("/decrement-products")
    @ResponseStatus(HttpStatus.OK)
    public void decrementProducts(@RequestBody StockUpdateRequest request) {
        inventoryService.decrementProducts(request.getIds(),request.getAmount());
    }

    @PostMapping("/increment-products")
    @ResponseStatus(HttpStatus.OK)
    public void incrementProducts(@RequestBody StockUpdateRequest request) {
        inventoryService.incrementProducts(request.getIds(),request.getAmount());
    }


    //    @PutMapping("/decrement-stock/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Inventory decrementStock (@PathVariable String id, @RequestBody InventoryRequest request)
//    {
//        return inventoryService.decrementStock(id,request);
//    }
//    @PutMapping ("/increment-stock/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Inventory incrementStock (@PathVariable String id, @RequestBody InventoryRequest request)
//    {
//        return inventoryService.incrementStock(id,request);
//    }
}