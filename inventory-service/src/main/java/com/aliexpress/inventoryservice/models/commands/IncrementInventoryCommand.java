package com.aliexpress.inventoryservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.inventoryservice.services.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class IncrementInventoryCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(DecrementInventoryCommand.class);
    private final InventoryService inventoryService;

    @Override
    public void execute(Message message) {
        try {
            OrderResponse orderResponse = objectMapper.readValue((String) message.getDataMap().get("OrderResponse"), OrderResponse.class);
            inventoryService.rollbackOrder(orderResponse);
        } catch (Exception e) {
            logger.info("Error" + e.getMessage());
        }

    }
}
