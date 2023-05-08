package com.aliexpress.orderservice.controllers;

import com.aliexpress.orderservice.models.*;
import com.aliexpress.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public Order getOrder(@PathVariable UUID id) {
        return order_service.getOrder(id);
    }

    @GetMapping("/user_orders/{user_id}")
    public List<Order> getUserOrders(@PathVariable UUID user_id) {
        return order_service.getUserOrders(user_id);
    }

    @PostMapping
    public void createOrder(@RequestBody Order order) {
        order_service.createOrder(order);
    }

    @PutMapping("/{id}")
    public void updateOrder(@PathVariable UUID id, @RequestBody Order order) {
        order_service.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        order_service.deleteOrder(id);
    }


}
