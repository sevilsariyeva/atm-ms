package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.service.TransactionTypeService;

public interface TransactionProvider {
    TransactionTypeService createTransactionType();
}
