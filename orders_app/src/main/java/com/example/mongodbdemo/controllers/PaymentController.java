package com.example.mongodbdemo.controllers;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import com.example.mongodbdemo.dtos.PaymentRequest;
import com.example.mongodbdemo.dtos.RefundRequest;

@RestController
public class PaymentController {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostMapping("/charges")
    public ResponseEntity<?> chargeCard(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Set your Stripe API key
            Stripe.apiKey = stripeApiKey;

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
            // Set your Stripe API key
            Stripe.apiKey = stripeApiKey;

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
    @GetMapping("/iman")
    public String iman(){
        return "iman";
    }

}
