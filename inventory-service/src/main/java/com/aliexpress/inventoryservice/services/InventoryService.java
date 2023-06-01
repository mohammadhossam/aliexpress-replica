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

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchangeInv.name}")
    private String exchangeNameInv;
    @Value("${rabbitmq.jsonBindingInvToPay.routingKey}")
    private String jsonRoutingKeyInvToPay;
    @Value("${rabbitmq.jsonBindingInvToOrd.routingKey}")
    private String jsonRoutingKeyInvToOrd;

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

    public Inventory incrementStock(String id, InventoryRequest request) {
        log.info("Increment stock");
        inventoryRepository.incrementStock(id, request.getQuantity());
        return inventoryRepository.findById(id).get();
    }

    public void decrementProducts(String[] ids, int[] amount) {
        log.info("Decrement products");
        log.info(ids.toString());
        inventoryRepository.decrementProducts(ids, amount);
    }

    public void incrementProducts(String[] ids, int[] amount) {
        log.info("Increment products");
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

    public void consumeOrder(OrderResponse orderResponse) {
        log.info(String.format("Received JSON message from OrderService => %s", orderResponse.toString()));
        try {
            decrementProducts(mapItemsDTOToIDs(orderResponse.getItems()),
                    mapItemsDTOToAmounts(orderResponse.getItems()));
            log.info("Stock decremented successfully!");
        } catch (Exception e) {
            Message message= mapOrderResponseToMessage(orderResponse,CommandEnum.DeleteOrderCommand,jsonRoutingKeyInvToOrd);
            sendJsonMessage(message, exchangeNameInv, jsonRoutingKeyInvToOrd);
            return;
        }
        Message message = mapOrderResponseToMessage(orderResponse,CommandEnum.PayToMerchantCommand,jsonRoutingKeyInvToPay);
        sendJsonMessage(message, exchangeNameInv, jsonRoutingKeyInvToPay);
        log.info(String.format("Sent JSON message to PaymentService => %s", message.toString()));
    }

    public void sendJsonMessage(Message message, String exchangeName, String jsonRoutingKey) {
        if(message!=null)
            rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, message);
    }

    public Message mapOrderResponseToMessage(OrderResponse orderResponse, CommandEnum command, String routingKey){
        String json=null;
        try{
            json = objectMapper.writeValueAsString(orderResponse);
        }
        catch(Exception e){
            log.info("Failed converting order response to JSON");
            return null;
        }
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("OrderResponse", json);
        Message message = Message.builder()
                .messageId(UUID.randomUUID().toString())
                .routingKey(routingKey)
                .messageDate(new Date())
                .command(command)
                .source("inventory")
                .dataMap(dataMap)
                .exchange(exchangeNameInv)
                .build();
        return message;
    }


    public void rollbackOrder(OrderResponse orderResponse) {
        log.info(String.format("Received JSON message from PaymentService => %s", orderResponse.toString()));
        try{
            incrementProducts(mapItemsDTOToIDs(orderResponse.getItems()),
                    mapItemsDTOToAmounts(orderResponse.getItems()));
            log.info("Stock incremented successfully!");
        }catch (Exception e ){
            log.info("Failed incrementing stock in rollback: "+e.getMessage());
        }
        Message message =  mapOrderResponseToMessage(orderResponse,CommandEnum.DeleteOrderCommand,jsonRoutingKeyInvToOrd);
        sendJsonMessage(message, exchangeNameInv, jsonRoutingKeyInvToOrd);
    }
}