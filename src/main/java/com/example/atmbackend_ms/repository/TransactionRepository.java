package com.example.atmbackend_ms.repository;

import com.example.atmbackend_ms.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByCardNumber(String cardNumber);
}
