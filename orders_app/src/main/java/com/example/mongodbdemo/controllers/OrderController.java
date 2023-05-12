package com.example.mongodbdemo.controllers;

import com.example.mongodbdemo.models.Order;
import com.example.mongodbdemo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    @Autowired
    private OrderService order_service;

    @RequestMapping("/orders")
    public List<Order> getAllOrders() {
        return order_service.getAllOrders();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orders/{id}")
    public Order getOrder(@PathVariable UUID id) {
        return order_service.getOrder(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userOrders/{user_id}")
    public List<Order> getUserOrders(@PathVariable UUID user_id) {
        return order_service.getUserOrders(user_id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orders")
    public void createOrder(@RequestBody Order order) {
        order_service.createOrder(order);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{id}")
    public void updateOrder(@PathVariable UUID id, @RequestBody Order order) {
        order_service.updateOrder(id, order);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/orders/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        order_service.deleteOrder(id);
    }


}
