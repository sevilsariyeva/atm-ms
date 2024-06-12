package com.example.atmbackend_ms.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public abstract class TransactionTypeService {
    public abstract String executeTransaction(String cardNumber, BigDecimal amount);
}
