package com.aliexpress.paymentservice.controller;

import com.aliexpress.paymentservice.dto.ChargeRequest;
import com.aliexpress.paymentservice.dto.RefundRequest;
import com.aliexpress.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService stripe_service;

    @PostMapping("/charges")
    public ResponseEntity<?> chargeCard(@RequestBody ChargeRequest paymentRequest) {
        try {

            // Get the customer object for the user
            Customer customer = Customer.retrieve(paymentRequest.getCustomerId());

            // Calculate the amount to be charged (in cents)
            int amountCents = (int) Math.round(paymentRequest.getAmount() * 100);

            // Create a new charge with the calculated amount and the customer's default payment source
            Charge charge = stripe_service.chargeCustomerCard(customer.getId(), amountCents);

            // Return a success response
            return ResponseEntity.ok(charge.getId());
        } catch (Exception e) {
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

            Refund refund = stripe_service.refund(chargeId);
            // Return a success response
            return ResponseEntity.ok(refund.getId());
        } catch (StripeException e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
