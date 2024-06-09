package com.example.atmbackend_ms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtmRepository{
    String validatePin(String cardNumber, int pin);
    String checkBalance(String cardNumber);
    String withDraw(String cardNumber, double amount);
    String deposit(String cardNumber, double amount);
}
