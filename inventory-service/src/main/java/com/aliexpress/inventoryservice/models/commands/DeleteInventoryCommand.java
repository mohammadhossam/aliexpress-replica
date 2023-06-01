package com.aliexpress.inventoryservice.models.commands;

import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteInventoryCommand implements Command {
    private final InventoryService inventoryService;
    @Override
    public void execute(Message message) {
        String productId= (String) message.getDataMap().get("productId");
        inventoryService.deleteProduct(productId);
    }
}
