package com.aliexpress.paymentservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PayToMerchantCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentService paymentService;
    @Override
    public void execute(Message message) {
        try {
            OrderResponse orderResponse = objectMapper.readValue((String) message.getDataMap().get("OrderResponse"), OrderResponse.class);
            paymentService.consumeOrder(orderResponse);
        } catch (Exception e) {
            log.info("Error executing PayToMerchantCommand: " + e.getMessage());
        }

    }
}
