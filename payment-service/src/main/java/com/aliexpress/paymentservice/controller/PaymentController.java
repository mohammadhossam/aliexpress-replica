package com.aliexpress.paymentservice.controller;

import com.aliexpress.paymentservice.dto.ChargeRequest;
import com.aliexpress.paymentservice.dto.PayoutRequest;
import com.aliexpress.paymentservice.dto.RefundRequest;
import com.aliexpress.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
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
    private PaymentService payment_service;

    @PostMapping("/charges")
    public ResponseEntity<?> chargeCard(@RequestBody ChargeRequest paymentRequest) {
        try {
            Charge charge= this.payment_service.chargeNewCard(paymentRequest);
            // Return a success response
            return ResponseEntity.ok(charge.getId());
        } catch (Exception e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    /*refund by charge id */
    @PostMapping("/refunds")
    public ResponseEntity<?> refundCharge(@RequestBody RefundRequest refundRequest) {
        try {
            String chargeId = refundRequest.getChargeId();

            Refund refund = payment_service.refund(chargeId);
            // Return a success response
            return ResponseEntity.ok(refund.getId());
        } catch (StripeException e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/payouts")
    public ResponseEntity<?> createPayout(@RequestBody PayoutRequest payoutRequest) {
        try {
            Payout payout = payment_service.payout(payoutRequest);
            // Return a success response
            return ResponseEntity.ok(payout.getId());
        } catch (StripeException e) {
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
