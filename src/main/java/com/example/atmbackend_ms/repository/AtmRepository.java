package com.example.atmbackend_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AtmRepository{
    String validatePin(String cardNumber, Integer pin);
    String checkBalance(String cardNumber);
    String withDraw(String cardNumber, BigDecimal amount);
    String deposit(String cardNumber, BigDecimal amount);
}
