package com.aliexpress.paymentservice.controller;

import com.aliexpress.paymentservice.dto.ChargeRequest;
import com.aliexpress.paymentservice.dto.PaymentRequest;
import com.aliexpress.paymentservice.dto.RefundRequest;
import com.aliexpress.paymentservice.service.StripeService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/charges")
    public ResponseEntity<?> chargeCard(@RequestBody PaymentRequest paymentRequest) {
        try {

            // Get the customer object for the user
            Customer customer = Customer.retrieve(paymentRequest.getCustomerId());

            // Get the ID of the customer's default payment source
            String sourceId = customer.getDefaultSource();

            // Calculate the amount to be charged (in cents)
            int amountCents = (int) Math.round(paymentRequest.getAmount() * 100);

            // Create a new charge with the calculated amount and the customer's default payment source
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amountCents);
            params.put("currency", "usd");
            params.put("customer", paymentRequest.getCustomerId());
            params.put("source", sourceId);
            Charge charge = Charge.create(params);

            // Return a success response
            return ResponseEntity.ok(charge.getId());
        } catch (StripeException e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    /*refund by charge id - todo we should link charge id with the order id (orders DB)
                              + update associated merchants accounts (which DB?) */
    @PostMapping("/refunds")
    public ResponseEntity<?> refundCharge(@RequestBody RefundRequest refundRequest) {
        try {
            String chargeId = refundRequest.getChargeId();

            Map<String, Object> params = new HashMap<>();
            params.put("charge", chargeId);
            Refund refund = Refund.create(params);

            // Return a success response
            return ResponseEntity.ok(refund.getId());
        } catch (StripeException e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
