package com.example.mongodbdemo;

import com.example.mongodbdemo.models.Item;
import com.example.mongodbdemo.models.Order;
import com.example.mongodbdemo.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class MongodbDemoApplicationTests {
    @Autowired
    OrderRepository repo;
   @Test
    void createOrder(){
       List<Item> items= new ArrayList<>();
       items.add(new Item(UUID.randomUUID(),2,40));
       items.add(new Item(UUID.randomUUID(),1,160.15 ));
        repo.save(new Order(UUID.randomUUID(),UUID.randomUUID(),"created",200.15,new Date(),items));
   }

}
