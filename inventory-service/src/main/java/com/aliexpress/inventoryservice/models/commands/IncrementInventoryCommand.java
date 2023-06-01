package com.aliexpress.inventoryservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class IncrementInventoryCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InventoryService inventoryService;

    @Override
    public void execute(Message message) {
        try {
            OrderResponse orderResponse = objectMapper.readValue((String) message.getDataMap().get("OrderResponse"), OrderResponse.class);
            inventoryService.rollbackOrder(orderResponse);
        } catch (Exception e) {
            log.info("Error executing IncrementInventoryCommand: " + e.getMessage());
        }

    }
}
