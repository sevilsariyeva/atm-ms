package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.service.DepositService;
import com.example.atmbackend_ms.service.BaseTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositProvider implements TransactionProvider{
    private final DepositService depositService;

    @Override
    public BaseTransactionService createTransactionType() {
        return depositService;
    }
}
