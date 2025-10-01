package com.example.ayush.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {


    @PostMapping("/register")
    public String register() {
        return "User registration endpoint.";
    }

    //permit all requests to "/"
    @GetMapping("/")
    public String home() {
        return "Welcome to the Bank Application!";
    }

    //authenticated requests to "/offers"
    @GetMapping("/offers")
    public String getOffers() {
        return "Here are the latest bank offers.";
    }

    //authenticated + authorization has role "USER" requests to "/balance"
    @GetMapping("/balance")
    public String getBalance() {
        return "Your balance is $1,000.";
    }

    //authenticated + authorization has role "ADMIN" requests to "/approveLoan"
    @GetMapping("/approveLoan")
    public String approveLoan() {
        return "Here are your loan details.";
    }

    @GetMapping("/denied")
    public String accessDenied() {
        return "Access Denied: You do not have permission to access this page.";
    }

    @PostMapping("/logout")
    public String logoutPage() {
        return "You have been logged out successfully.";
    }
}
