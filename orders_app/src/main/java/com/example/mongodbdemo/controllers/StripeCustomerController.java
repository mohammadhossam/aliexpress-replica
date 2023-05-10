//package com.example.mongodbdemo.controllers;
//import com.example.mongodbdemo.dtos.CustomerRequest;
//import com.example.mongodbdemo.services.StripeService;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class StripeCustomerController {
//
//    @Autowired
//    private StripeService stripeService;
//
//    @PostMapping("/customers")
//    public Customer createCustomer(@RequestBody CustomerRequest request) throws StripeException {
//        String email = request.getEmail();
//        String source = request.getSource();
//        return stripeService.createCustomer(email, source);
//    }
//}
