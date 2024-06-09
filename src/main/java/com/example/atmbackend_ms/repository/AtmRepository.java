package com.example.atmbackend_ms.repository;

import com.example.atmbackend_ms.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AtmRepository extends JpaRepository<Account,Long>{

}
