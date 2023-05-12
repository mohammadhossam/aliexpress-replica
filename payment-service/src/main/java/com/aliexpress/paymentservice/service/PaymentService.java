package com.aliexpress.paymentservice.service;

import com.aliexpress.paymentservice.dto.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.RateLimitException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    private void setSecretKey(){
        Stripe.apiKey= secretKey;
    }
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);


    public Customer createCustomer(String token, String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        return Customer.create(customerParams);
    }

    private Customer getCustomer(String id) throws StripeException {
        return Customer.retrieve(id);
    }

    public Charge chargeNewCard(String token, double amount) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }

    public Charge chargeCustomerCard(String customerId, int amount) throws StripeException {
        //todo - hard-coded token from nada
        String sourceCard = getCustomer(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = null;
        try {
            charge = Charge.create(chargeParams);
        } catch (CardException e) {
            //todo - in case of err send msg to inventory service
//            System.out.println("Status is: " + e.getCode());
//            System.out.println("Message is: " + e.getMessage());
        }
        return charge;
    }
    public Refund refund(String chargeId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("charge", chargeId);
        Refund refund = Refund.create(params);
        return refund;
    }
    @RabbitListener(queues = {"${rabbitmq.jsonQueueInvToPay.name}"})
    public void consume(OrderResponse orderResponse) throws JsonProcessingException {
        logger.info(String.format("Received Json message => %s", orderResponse.toString()));

    }
    //todo
    /*
    * trigger payment - both user and merchant side - simulation only
    * if any err - undo any changes + trigger inventory service
    * if all good then tmam
    * */
}