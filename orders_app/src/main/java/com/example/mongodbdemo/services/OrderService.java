package com.example.mongodbdemo.services;

import com.example.mongodbdemo.models.Order;
import com.example.mongodbdemo.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;
    public List<Order> getAllOrders(){
        return repo.findAll();
    }
    public Order getOrder(UUID id){
        return repo.findById(id).get();
    }
    public List<Order> getUserOrders(UUID user_id){
        return repo.findOrdersByUser_id(user_id);
    }
    public void createOrder(Order order){
        repo.save(order);
    }
    public void updateOrder(UUID id, Order order){
        repo.save(order);
    }
    public void deleteOrder(UUID id){
        repo.deleteById(id);
    }


}
