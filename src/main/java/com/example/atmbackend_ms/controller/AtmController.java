package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.repository.AtmRepository;
import com.example.atmbackend_ms.service.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/atm")
public class AtmController {
    @Autowired
    private AtmService atmService;

    @PostMapping("/enter-pin")
    public String enterPin(@RequestParam String cardNumber, @RequestParam int pin){
        return atmService.validatePin(cardNumber,pin);
    }

    @GetMapping("/balance")
    public String checkBalance(@RequestParam String cardNumber){
        return atmService.checkBalance(cardNumber);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String cardNumber, @RequestParam BigDecimal amount){
        return atmService.withDraw(cardNumber,amount);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String cardNumber, @RequestParam BigDecimal amount){
        return atmService.deposit(cardNumber,amount);
    }
}
