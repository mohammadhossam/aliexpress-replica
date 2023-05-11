package com.aliexpress.inventoryservice.services;
;
import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.dto.OrderRequest;
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
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
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

    @RabbitListener(queues = {"${rabbitmq.jsonQueue.name}"})
    public void consume(String order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<OrderRequest> mapType = new TypeReference<>() {};
        OrderRequest payload = objectMapper.readValue(order, mapType);
        logger.info(String.format("Received Json message => %s", payload.toString()));
    }
}