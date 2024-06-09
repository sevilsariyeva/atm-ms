package com.example.atmbackend_ms.repository;

import com.example.atmbackend_ms.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByCardNumber(String cardNumber);
}
