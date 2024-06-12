package com.example.atmbackend_ms.model;

import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.model.enums.TransferType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
