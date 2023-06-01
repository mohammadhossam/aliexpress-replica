package com.aliexpress.inventoryservice.services;

import com.aliexpress.commondtos.ItemDTO;
import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.CommandEnum;
import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.models.Inventory;
import com.aliexpress.inventoryservice.repositories.InventoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final InventoryRepository inventoryRepository;
    @Value("${rabbitmq.exchangeInv.name}")
    private String exchangeNameInv;
    @Value("${rabbitmq.jsonBindingInvToPay.routingKey}")
    private String jsonRoutingKeyInvToPay;
    @Value("${rabbitmq.jsonBindingInvToOrd.routingKey}")
    private String jsonRoutingKeyInvToOrd;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);


    public Inventory createProduct(InventoryRequest request) {
        String id= request.getId();
        int quantity= request.getQuantity();
        inventoryRepository.createInventory(id, quantity);
        log.info(String.format("Inventory for product: %s is created with quantity: %d", id, quantity));
        return inventoryRepository.findById(id).get();
    }

    public int getProductData(String id) {
        log.info("Get Inventory");
        return inventoryRepository.getInventoryById(id);
    }

    public Inventory updateProduct(String id, InventoryRequest request) {
        log.info("Update Inventory");
        inventoryRepository.updateInventory(id, request.getQuantity());
        return inventoryRepository.findById(id).get();
    }

    public void deleteProduct(String id) {
        inventoryRepository.deleteInventory(id);
        log.info(String.format("Inventory for product: %s is deleted", id));
    }

    public Inventory decrementStock(String id, InventoryRequest request) {
        log.info("Decrement stock");
        inventoryRepository.decrementStock(id, request.getQuantity());
        return inventoryRepository.findById(id).get();
    }

    @SneakyThrows
    public Inventory incrementStock(String id, InventoryRequest request) {
        log.info("Increment stock");
        inventoryRepository.incrementStock(id, request.getQuantity());
        return inventoryRepository.findById(id).get();
    }

    public void decrementProducts(String[] ids, int[] amount) {
        log.info("decrement products");
        log.info(ids.toString());
        inventoryRepository.decrementProducts(ids, amount);
    }

    public void incrementProducts(String[] ids, int[] amount) {
        log.info("increment products");
        log.info(ids.toString());
        inventoryRepository.incrementProducts(ids, amount);
    }

    public String[] mapItemsDTOToIDs(List<ItemDTO> itemsDTO) {
        return Arrays.stream(itemsDTO.stream().map(item -> item.getId()).toArray())
                .map(Object::toString)
                .toArray(String[]::new);

    }

    public int[] mapItemsDTOToAmounts(List<ItemDTO> itemsDTO) {
        return itemsDTO.stream().mapToInt(item -> item.getQuantity()).toArray();
    }

//    @RabbitListener(queues = {"${rabbitmq.jsonQueueOrdToInv.name}"})
    public void consumeOrder(OrderResponse orderResponse) {
        logger.info(String.format("Received Json message => %s", orderResponse.toString()));
        try {
            decrementProducts(mapItemsDTOToIDs(orderResponse.getItems()),
                    mapItemsDTOToAmounts(orderResponse.getItems()));
            logger.info("Stock update successful!");
        } catch (Exception e) {
            sendJsonMessage(orderResponse, exchangeNameInv, jsonRoutingKeyInvToOrd);
            return;
        }
        logger.info(String.format("Sent JSON message => %s", orderResponse.toString()));
        String json = null;
        try {
            json = objectMapper.writeValueAsString(orderResponse);
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("OrderResponse", json);
            Message message = Message.builder()
                    .messageId(UUID.randomUUID().toString())
                    .routingKey(jsonRoutingKeyInvToPay)
                    .messageDate(new Date())
                    .command(CommandEnum.PayToMerchantCommand)
                    .source("inventory")
                    .dataMap(dataMap)
                    .exchange(exchangeNameInv)
                    .build();
            sendJsonMessage(message, exchangeNameInv, jsonRoutingKeyInvToPay);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    public void sendJsonMessage(Message message, String exchangeName, String jsonRoutingKey) {
        logger.info(String.format("Sent JSON message => %s", message.toString()));
        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, message);
    }
    public void sendJsonMessage(OrderResponse orderResponse, String exchangeName, String jsonRoutingKey) {
//        logger.info(String.format("Sent JSON message => %s", orderResponse.toString()));
//        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, orderResponse);
        try {
            logger.info(String.format("Sent JSON message => %s", orderResponse.toString()));
            String json = objectMapper.writeValueAsString(orderResponse);

            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("OrderResponse", json);
            Message message = Message.builder()
                    .messageId(UUID.randomUUID().toString())
                    .routingKey(jsonRoutingKey)
                    .messageDate(new Date())
                    .command(CommandEnum.DeleteOrderCommand)
                    .source("inventory")
                    .dataMap(dataMap)
                    .exchange(exchangeName)
                    .build();
            rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, message);
        } catch (Exception e) {
            logger.info("Error " + e.getMessage());
        }
    }

    public void rollbackOrder(OrderResponse orderResponse) {
        incrementProducts(mapItemsDTOToIDs(orderResponse.getItems()),
                mapItemsDTOToAmounts(orderResponse.getItems()));
        sendJsonMessage(orderResponse, exchangeNameInv, jsonRoutingKeyInvToOrd);
    }
}