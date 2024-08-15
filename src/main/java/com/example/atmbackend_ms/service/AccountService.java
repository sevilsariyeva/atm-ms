package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public boolean isCardNumberUnique(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber).isEmpty();
    }

    public String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (!isCardNumberUnique(cardNumber));
        return cardNumber;
    }

    public Integer generatePin() {
        return (int) (Math.random() * 9000) + 1000;
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        return cardNumber.toString();
    }

    public Account registerAccount(String fullname, String email) {
        String cardNumber = generateUniqueCardNumber();
        Integer pin = generatePin();

        Account account = Account.builder()
                .fullname(fullname)
                .email(email)
                .cardNumber(cardNumber)
                .balance(BigDecimal.ZERO)
                .pin(pin)
                .build();
        accountRepository.save(account);

        return account;
    }
}
