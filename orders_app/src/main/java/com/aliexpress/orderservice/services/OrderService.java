package com.aliexpress.orderservice.services;

import com.aliexpress.orderservice.dto.ItemDTO;
import com.aliexpress.orderservice.dto.OrderRequest;
import com.aliexpress.orderservice.dto.OrderResponse;
import com.aliexpress.orderservice.models.Item;
import com.aliexpress.orderservice.models.Order;
import com.aliexpress.orderservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;
    @Value("${rabbitmq.exchangeOrdersToInv.name}")
    private String exchangeName;
    @Value("${rabbitmq.jsonBindingOrdersToInv.routingKey}")
    private String jsonRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger= LoggerFactory.getLogger(OrderService.class);

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
        sendJsonMessage(mapToOrderResponse(order));
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
                .map(this::mapItemDTOToItem)
                .toList();
        order.setItems(items);
        repo.save(order);
    }
    private Item mapItemDTOToItem(ItemDTO itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        return item;
    }
    private ItemDTO mapItemToItemDTO(Item item) {
        ItemDTO itemDto = new ItemDTO();
        item.setId(item.getId());
        item.setPrice(item.getPrice());
        item.setQuantity(item.getQuantity());
        return itemDto;
    }
    public OrderResponse mapToOrderResponse(Order order){
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

}
