//package com.example.mongodbdemo.services;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Customer;
//import com.stripe.param.CustomerCreateParams;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StripeService {
//
//    @Value("${stripe.api.key}")
//    private String apiKey;
//
//    public Customer createCustomer(String email, String source) throws StripeException {
//        Stripe.apiKey = apiKey;
//
//        CustomerCreateParams params =
//                CustomerCreateParams.builder()
//                        .setEmail(email)
//                        .setSource(source)
//                        .build();
//
//        return Customer.create(params);
//    }
//}
