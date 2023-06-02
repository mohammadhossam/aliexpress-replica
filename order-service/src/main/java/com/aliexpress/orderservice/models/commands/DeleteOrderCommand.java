package com.aliexpress.orderservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.orderservice.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("DeleteOrderCommand")
public class DeleteOrderCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService;

    @Override
    public void execute(Message message) {
        try {
            OrderResponse orderResponse = objectMapper.readValue((String) message.getDataMap().get("OrderResponse"), OrderResponse.class);
            orderService.deleteOrder(orderResponse.getId());
        } catch (Exception e) {
            log.info("Error executing DeleteOrderCommand: " + e.getMessage());
        }
    }
}
