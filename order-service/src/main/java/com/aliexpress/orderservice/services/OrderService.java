package com.aliexpress.orderservice.services;

import com.aliexpress.commondtos.ItemDTO;
import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.CommandEnum;
import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final ObjectMapper objectMapper = new ObjectMapper();
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
        Order order=mapToOrder(orderRequest, UUID.randomUUID().toString());
        sendJsonMessage(mapToOrderResponse(order));
    }

    public void updateOrder(String orderId, OrderRequest orderRequest) {
        mapToOrder(orderRequest, orderId);
    }

    public void deleteOrder(String id) {
        Order order = repo.deleteOrderById(id);
        if(order!=null)
            removeFromCache(order.getUser_id());
    }

    public void removeFromCache(String user_id) {
        if (redisTemplate.hasKey(user_id))
            redisTemplate.delete(user_id);
    }

    private Order mapToOrder(OrderRequest orderRequest, String orderId) {
        List<Item> items = orderRequest.getItems()
                .stream()
                .map(this::mapItemDTOToItem)
                .toList();
        Order order = Order.builder()
                .id(orderId)
                .user_id(orderRequest.getUser_id())
                .status("created").date(new Date())
                .items(items)
                .total_price(calculateTotalPrice(items))
                .shipping((new Random().nextInt(3) + 1) * 5)
                .build();
        repo.save(order);
        removeFromCache(order.getUser_id());
        return order;
    }

    private Item mapItemDTOToItem(ItemDTO itemDto) {
        Item item = Item.builder()
                .id(itemDto.getId())
                .merchantId(itemDto.getMerchantId())
                .price(itemDto.getPrice())
                .quantity(itemDto.getQuantity())
                .build();
        return item;
    }

    private ItemDTO mapItemToItemDTO(Item item) {
        ItemDTO itemDto = ItemDTO.builder()
                .id(item.getId())
                .merchantId(item.getMerchantId())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
        return itemDto;
    }

    private double calculateTotalPrice(List<Item> items) {
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<ItemDTO> itemsDto = order.getItems()
                .stream()
                .map(this::mapItemToItemDTO)
                .toList();
        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .date(new Date())
                .items(itemsDto)
                .shipping(order.getShipping())
                .status(order.getStatus())
                .total_price(order.getTotal_price())
                .user_id(order.getUser_id())
                .build();
        return orderResponse;
    }

    public void sendJsonMessage(OrderResponse order) {
        try {
            logger.info(String.format("Sent JSON message => %s", order.toString()));
            String json = objectMapper.writeValueAsString(order);

            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("OrderResponse", json);
            Message message = Message.builder()
                    .messageId(UUID.randomUUID().toString())
                    .routingKey(jsonRoutingKey)
                    .messageDate(new Date())
                    .command(CommandEnum.DecrementInventoryCommand)
                    .source("orders")
                    .dataMap(dataMap)
                    .exchange(exchangeName)
                    .build();
            rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, message);
        } catch (Exception e) {
            logger.info("Error " + e.getMessage());
        }

    }

}