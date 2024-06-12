package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.service.DepositService;
import com.example.atmbackend_ms.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositProvider implements TransactionProvider{
    private final DepositService depositService;

    @Override
    public TransactionTypeService createTransactionType() {
        return depositService;
    }
}
