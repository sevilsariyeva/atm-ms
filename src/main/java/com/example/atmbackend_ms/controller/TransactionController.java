package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/execute/type")
    public String transaction(@RequestParam TransactionType transactionType, @RequestParam String cardNumber, @RequestParam BigDecimal amount){
        return transactionService.executeTransaction(transactionType, cardNumber, amount);
    }
}
