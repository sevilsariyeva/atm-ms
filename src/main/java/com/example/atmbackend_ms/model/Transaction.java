package com.example.atmbackend_ms.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "transactions")
public class Transaction<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String fromCardNumber;
    private String toCardNumber;
    private LocalDateTime timestamp;

    private T type;

    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
}
