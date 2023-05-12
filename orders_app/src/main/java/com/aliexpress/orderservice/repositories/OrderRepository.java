package com.aliexpress.orderservice.repositories;

import com.aliexpress.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, String> {
    @Query("{'user_id':?0}")
    List<Order> findOrdersByUser_id(String user_id);

}
