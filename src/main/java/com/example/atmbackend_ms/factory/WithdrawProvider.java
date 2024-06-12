package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.service.TransactionTypeService;
import com.example.atmbackend_ms.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawProvider implements TransactionProvider{
    private final WithdrawService withdrawService;
    @Override
    public TransactionTypeService createTransactionType() {
        return withdrawService;
    }
}
