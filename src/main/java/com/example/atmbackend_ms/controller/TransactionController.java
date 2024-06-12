package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/execute/type/{transactionType}/{cardNumber}/{amount}")
    public String transaction(@PathVariable TransactionType transactionType,
                              @PathVariable String cardNumber,
                              @PathVariable BigDecimal amount){
        return transactionService.executeTransaction(transactionType, cardNumber, amount);
    }
}
