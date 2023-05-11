package com.aliexpress.inventoryservice.services;
;
import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.models.Inventory;
import com.aliexpress.inventoryservice.repositories.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

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

}