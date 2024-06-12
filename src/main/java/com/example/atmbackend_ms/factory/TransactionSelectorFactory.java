package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.model.enums.TransactionType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class TransactionSelectorFactory {
    @Autowired
    private WithdrawProvider withdrawProvider;
    @Autowired
    private DepositProvider depositProvider;

    public TransactionProvider selectTransactionProvider(TransactionType type){
        return switch (type) {
            case WITHDRAW -> withdrawProvider;
            case DEPOSIT -> depositProvider;
            default -> throw new AccountNotFoundException("Provided TransactionType Not found");
        };
    }
}
