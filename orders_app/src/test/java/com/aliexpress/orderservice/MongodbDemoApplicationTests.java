package com.aliexpress.orderservice;

import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MongodbDemoApplicationTests {
    @Autowired
    OrderRepository repo;
   @Test
    void createOrder(){
       Order order= new Order();
       order.setUser_id("dummy");
       order.setStatus("created");
       order.setTotal_price(200.15);
       order.setDate(new Date());
       List<Item> items= new ArrayList<>();
       items.add(new Item("dummy",2,40));
       items.add(new Item("dummy",1,160.15 ));
       order.setItems(items);
        repo.save(order);
   }

}
