package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.service.AtmService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class AtmServiceFactory {
    @Autowired
    private WithdrawService withDrawService;
    private DepositService depositService;

    public TransactionService getMethod(TransactionType type){
        return switch (type) {
            case WITHDRAW -> withDrawService;
            case DEPOSIT -> depositService;
            default -> null;
        };
    }
}
