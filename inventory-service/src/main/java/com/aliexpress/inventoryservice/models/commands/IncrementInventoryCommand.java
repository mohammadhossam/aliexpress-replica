package com.aliexpress.inventoryservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IncrementInventoryCommand implements Command {
    private final InventoryService inventoryService;
    @Override
    public void execute(Message message) {
        OrderResponse orderResponse= (OrderResponse) message.getDataMap().get("OrderResponse");
        inventoryService.orderRollback(orderResponse);
    }
}
