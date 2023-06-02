package com.aliexpress.orderservice.services;

import com.aliexpress.commondtos.ItemDTO;
import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.CommandEnum;
import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class OrderService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private OrderRepository repo;
    @Value("${rabbitmq.exchangeOrdToInv.name}")
    private String exchangeName;
    @Value("${rabbitmq.jsonBindingOrdToInv.routingKey}")
    private String jsonRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    public Order getOrder(String id) {
        return repo.findById(id).get();
    }

    public List<Order> getUserOrders(String user_id) {
        List<Order> res;
        if (redisTemplate.hasKey(user_id)) {
            log.info(String.format("Get user: %s orders from cache",user_id));
            res = (List<Order>) redisTemplate.opsForValue().get(user_id);
        } else {
            log.info(String.format("Get user: %s orders from DB",user_id));
            res = repo.findOrdersByUser_id(user_id);
            redisTemplate.opsForValue().set(user_id, res);
        }
        return res;
    }

    public ResponseEntity<String> createOrder(OrderRequest orderRequest, String authHeader) {
        if (authHeader != null) {
            String token = authHeader.substring(7);
            String tokenKey = "buyer_token:" + orderRequest.getUser_id();
            String tokenFromRedis = (String) redisTemplate.opsForValue().get(tokenKey);
            if (tokenFromRedis == null || !tokenFromRedis.equals(token)) {
                System.out.println("Token is not valid");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authenticated!");
            } else {
                Order order=mapToOrder(orderRequest, UUID.randomUUID().toString());
                sendJsonMessage(mapToOrderResponse(order));
                return ResponseEntity.ok("Order created successfully");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("No authorization header exists!");
        }
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
            log.info(String.format("Sent JSON message to inventory => %s", order.toString()));
        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
        }
    }
}
