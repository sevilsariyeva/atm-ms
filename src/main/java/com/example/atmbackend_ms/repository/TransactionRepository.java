package com.example.atmbackend_ms.repository;

import com.example.atmbackend_ms.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {
    //List<Transaction> findByCardNumber(String cardNumber);
}
