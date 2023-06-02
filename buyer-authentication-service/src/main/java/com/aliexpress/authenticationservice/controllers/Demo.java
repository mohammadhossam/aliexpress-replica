package com.aliexpress.authenticationservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class Demo {

    @GetMapping("/test")
    public String test() {
        return "User is authenticated!";
    }
}
