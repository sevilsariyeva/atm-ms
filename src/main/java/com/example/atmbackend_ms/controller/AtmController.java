package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.service.AtmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmService;

    @PostMapping("/pin")
    public String enterPin(@RequestParam String cardNumber, @RequestParam Integer pin){
        return atmService.validatePin(cardNumber, pin);
    }

    @GetMapping("/balance")
    public String checkBalance(@RequestParam String cardNumber){
        return atmService.checkBalance(cardNumber);
    }
}
