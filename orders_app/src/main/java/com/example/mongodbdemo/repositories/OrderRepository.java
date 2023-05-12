package com.example.mongodbdemo.repositories;

import com.example.mongodbdemo.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, UUID> {
    @Query("{'user_id':?0}")
    List<Order> findOrdersByUser_id(UUID user_id);

}
