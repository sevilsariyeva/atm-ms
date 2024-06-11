package com.example.atmbackend_ms.model;

import com.example.atmbackend_ms.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCardNumber;
    private String toCardNumber;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
}
