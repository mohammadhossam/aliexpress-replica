package com.aliexpress.orderservice.services;

import com.aliexpress.orderservice.dto.ItemDTO;
import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.dto.OrderResponse;
import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private RedisTemplate<String, List<Order>> redisTemplate;

    @Autowired
    private OrderRepository repo;
    @Value("${rabbitmq.exchangeOrdToInv.name}")
    private String exchangeName;
    @Value("${rabbitmq.jsonBindingOrdToInv.routingKey}")
    private String jsonRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    public Order getOrder(String id) {
        return repo.findById(id).get();
    }

    public List<Order> getUserOrders(String user_id) {
        List<Order> res;
        if (redisTemplate.hasKey(user_id)) {
            logger.info("Get from cache");
            res = redisTemplate.opsForValue().get(user_id);
        } else {
            logger.info("Get from DB");
            res = repo.findOrdersByUser_id(user_id);
            redisTemplate.opsForValue().set(user_id, res);
        }
        return res;
    }

    public void createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        mapToOrder(orderRequest, order);
        sendJsonMessage(mapToOrderResponse(order));
    }

    public void updateOrder(String id, OrderRequest orderRequest) {
        Order updatedOrder = new Order();
        mapToOrder(orderRequest, updatedOrder);
    }

    public void deleteOrder(String id) {
        Order order = repo.deleteOrderById(id);
        removeFromCache(order.getUser_id());
    }

    public void removeFromCache(String user_id) {
        if (redisTemplate.hasKey(user_id))
            redisTemplate.delete(user_id);
    }

    private void mapToOrder(OrderRequest orderRequest, Order order) {
        order.setUser_id(orderRequest.getUser_id());
        order.setStatus(orderRequest.getStatus());
        order.setDate(orderRequest.getDate());
        List<Item> items = orderRequest.getItems()
                .stream()
                .map(this::mapItemDTOToItem)
                .toList();
        order.setItems(items);
        order.setTotal_price(orderRequest.getTotal_price());
        repo.save(order);
        removeFromCache(order.getUser_id());
    }

    private Item mapItemDTOToItem(ItemDTO itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        item.setMerchantId(itemDto.getMerchantId());
        return item;
    }

    private ItemDTO mapItemToItemDTO(Item item) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setPrice(item.getPrice());
        itemDto.setQuantity(item.getQuantity());
        itemDto.setMerchantId(item.getMerchantId());
        return itemDto;
    }

    public OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setDate(order.getDate());
        orderResponse.setUser_id(order.getUser_id());
        orderResponse.setTotal_price(order.getTotal_price());
        orderResponse.setStatus(order.getStatus());
        List<ItemDTO> itemsDto = order.getItems()
                .stream()
                .map(this::mapItemToItemDTO)
                .toList();
        orderResponse.setItems(itemsDto);
        return orderResponse;
    }

    public void sendJsonMessage(OrderResponse order) {
        logger.info(String.format("Sent JSON message => %s", order.toString()));
        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, order);
    }

    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToOrd.name}"})
    public void orderRollback(OrderResponse orderResponse) {
        deleteOrder(orderResponse.getId());
    }

}
