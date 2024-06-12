package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.factory.TransactionSelectorFactory;
import com.example.atmbackend_ms.model.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionSelectorFactory transactionSelectorFactory;

    public String executeTransaction(TransactionType transactionType, String cardNumber, BigDecimal amount) {
        return transactionSelectorFactory
                .selectTransactionProvider(transactionType)
                .createTransactionType()
                .executeTransaction(cardNumber, amount);
    }
}
