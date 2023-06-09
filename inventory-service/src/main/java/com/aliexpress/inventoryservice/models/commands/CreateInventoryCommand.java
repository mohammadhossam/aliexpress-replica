package com.aliexpress.inventoryservice.models.commands;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.dto.InventoryRequest;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("CreateInventoryCommand")
public class CreateInventoryCommand implements Command {
    private final InventoryService inventoryService;
    @Override
    public void execute(Message message) {
        String productId= (String) message.getDataMap().get("productId");
        int productQuantity= (int) message.getDataMap().get("quantity");
        InventoryRequest request= InventoryRequest.builder()
                .id(productId)
                .quantity(productQuantity)
                .build();
        inventoryService.createProduct(request);
    }
}
