package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.service.AtmService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AtmServiceFactory {
    private final AtmService withDrawService;
    private final AtmService depositService;

    public AtmService getMethod(TransactionType type){
        if(type.equals(TransactionType.WITHDRAW)){
            return withDrawService;
        }else if(type.equals(TransactionType.DEPOSIT)){
            return depositService;
        }else{
            return null;
        }
    }
}
