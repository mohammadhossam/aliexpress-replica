package com.example.mongodbdemo.repositories;

import com.example.mongodbdemo.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, UUID> {

}
