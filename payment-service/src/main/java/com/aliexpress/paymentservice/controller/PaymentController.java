package com.aliexpress.paymentservice.controller;

import com.aliexpress.paymentservice.dto.ChargeRequest;
import com.aliexpress.paymentservice.service.StripeService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private StripeService stripe_service;

    @PostMapping
    public String chargeCard(@RequestBody ChargeRequest request) throws Exception {
//        String token = request.getStripeToken();
        Map<String, Object> card = new HashMap<>();
        card.put("number", "4242424242424242");
        card.put("exp_month", 5);
        card.put("exp_year", 2024);
        card.put("cvc", "314");
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);

        Token token = Token.create(params);

        Double amount = request.getAmount();

        return this.stripe_service.chargeNewCard(token.getId(), amount).toJson();
    }
}
