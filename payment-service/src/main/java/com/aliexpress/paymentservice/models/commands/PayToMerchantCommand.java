package com.aliexpress.paymentservice.models.commands;

import com.aliexpress.commondtos.OrderResponse;
import com.aliexpress.commonmodels.Message;
import com.aliexpress.commonmodels.commands.Command;
import com.aliexpress.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class PayToMerchantCommand implements Command {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(PayToMerchantCommand.class);
    private final PaymentService paymentService;
    @Override
    public void execute(Message message) {
        try {
            OrderResponse orderResponse = objectMapper.readValue((String) message.getDataMap().get("OrderResponse"), OrderResponse.class);
            logger.info("Payment response: "+orderResponse.toString());
            paymentService.consumeOrder(orderResponse);
        } catch (Exception e) {
            logger.info("Error" + e.getMessage());
        }

    }
}
