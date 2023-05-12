package com.aliexpress.orderservice.services;

import com.aliexpress.orderservice.dto.ItemDTO;
import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;

    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    public Order getOrder(String id) {
        return repo.findById(id).get();
    }

    public List<Order> getUserOrders(String user_id) {
        return repo.findOrdersByUser_id(user_id);
    }

    public void createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        mapToOrder(orderRequest, order);
    }

    public void updateOrder(String id, OrderRequest orderRequest) {
        Order updatedOrder = new Order();
        mapToOrder(orderRequest, updatedOrder);
    }

    public void deleteOrder(String id) {
        repo.deleteById(id);
    }
    private void mapToOrder(OrderRequest orderRequest, Order order) {
        order.setUser_id(orderRequest.getUser_id());
        order.setStatus(orderRequest.getStatus());
        order.setDate(orderRequest.getDate());
        List<Item> items = orderRequest.getItems()
                .stream()
                .map(this::mapToItem)
                .toList();
        order.setItems(items);
        repo.save(order);
    }
    private Item mapToItem(ItemDTO itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        return item;
    }

}
