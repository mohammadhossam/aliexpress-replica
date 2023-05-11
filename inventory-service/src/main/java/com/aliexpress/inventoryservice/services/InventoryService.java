package com.aliexpress.inventoryservice.services;
;
import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.dto.OrderRequest;
import com.aliexpress.inventoryservice.dto.OrderResponse;
import com.aliexpress.inventoryservice.models.Inventory;
import com.aliexpress.inventoryservice.repositories.InventoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Value("${rabbitmq.exchangeInvToPay.name}")
    private String exchangeName;
    @Value("${rabbitmq.jsonBindingInvToPay.routingKey}")
    private String jsonRoutingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static final Logger logger= LoggerFactory.getLogger(InventoryService.class);



    @SneakyThrows
    public Inventory createProduct(InventoryRequest request) {
        log.info("Checking Inventory");
        inventoryRepository.createInventory(request.getId(), request.getQuantity());
        return inventoryRepository.findById(request.getId()).get();
    }
    @SneakyThrows
    public int getProductData(String id) {
        log.info("Get Inventory");
        return inventoryRepository.getInventoryById(id);
    }

    @SneakyThrows
    public Inventory updateProduct(String id, InventoryRequest request) {
        log.info("Update Inventory");
        inventoryRepository.updateInventory(id, request.getQuantity());
        return inventoryRepository.findById(id).get();
    }

    @SneakyThrows
    public void deleteProduct(String id) {
        log.info("Delete Inventory");
        inventoryRepository.deleteInventory(id);
    }

    @SneakyThrows
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

    @RabbitListener(queues = {"${rabbitmq.jsonQueueOrdersToInv.name}"})
    public void consume(OrderResponse orderResponse) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(String.format("Received Json message => %s", orderResponse.toString()));


        /* todo
        * go to DB and do the checks and the looping
        * in case of problem - undo changes + trigger order service
        * in case all is good - send order to payment thru MQ
        * do we need to handle user auth???
        * */
    }
    public void sendJsonMessage(OrderResponse order) {
        logger.info(String.format("Sent JSON message => %s", order.toString()));
        rabbitTemplate.convertAndSend(exchangeName, jsonRoutingKey, order);
    }
    //todo - receive a rollback from payment
}