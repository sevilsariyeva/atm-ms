package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.service.BaseTransactionService;

public interface TransactionProvider {
    BaseTransactionService createTransactionType();
}
