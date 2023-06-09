package com.aliexpress.orderservice.controllers;

import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.models.*;
import com.aliexpress.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService order_service;

    @GetMapping
    public List<Order> getAllOrders() {
        return order_service.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        return order_service.getOrder(id);
    }

    @GetMapping("/user_orders/{user_id}")
    public ResponseEntity<?> getUserOrders(@PathVariable String user_id, @RequestHeader(value = "Authorization") String authHeader) {
         return order_service.getUserOrders(user_id, authHeader);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest order, @RequestHeader(value = "Authorization") String authHeader) {
        return order_service.createOrder(order, authHeader);
    }

    @PutMapping("/{id}")
    public void updateOrder(@PathVariable String id, @RequestBody OrderRequest order) {
        order_service.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        order_service.deleteOrder(id);
    }

}
