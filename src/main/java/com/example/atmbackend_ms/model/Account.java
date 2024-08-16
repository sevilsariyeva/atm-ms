package com.example.atmbackend_ms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;

    private String email;

    @Column(unique = true)
    private String cardNumber;

    private String pin;

    private BigDecimal balance= BigDecimal.ZERO;
    private BigDecimal deposit= BigDecimal.ZERO;

    @Version
    private Long version;

}
