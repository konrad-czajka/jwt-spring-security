package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/login")
    public String login() {
        return "logged in";
    }

    @GetMapping("/data")
    public String getData() {
        return "secure data";
    }
}
