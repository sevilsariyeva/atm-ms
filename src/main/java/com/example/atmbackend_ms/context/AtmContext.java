package com.example.atmbackend_ms.context;

import com.example.atmbackend_ms.model.enums.AtmState;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
@Builder
public class AtmContext {
    private AtmState atmState=AtmState.READY;
    private String cardNumber;
}
