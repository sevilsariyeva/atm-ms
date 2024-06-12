package com.example.atmbackend_ms.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public abstract class BaseTransactionService {
    public abstract String executeTransaction(String cardNumber, BigDecimal amount);
}
