package com.example.atmbackend_ms.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AtmServiceFactory {
    private final AtmService enterPinService;
    private final AtmService withDrawService;
    private final AtmService depositService;

    public AtmService getMethod(String method){
        if(method.equals("enter-pin")){
            return enterPinService;
        } else if (method.equals("withdraw")) {
            return withDrawService;
        }else if(method.equals("deposit")){
            return depositService;
        }else{
            return null;
        }
    }
}
