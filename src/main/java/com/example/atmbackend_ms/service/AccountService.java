package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean isCardNumberUnique(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber).isEmpty();
    }
    public Optional<Account> findByCardNumber(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber);
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
        String prefix = "40980944";
        StringBuilder cardNumber = new StringBuilder(prefix);

        for (int i = 0; i < 16 - prefix.length(); i++) {
            cardNumber.append((int) (Math.random() * 10));
        }

        return cardNumber.toString();
    }

    public Account registerAccount(String fullname, String email) {
        String cardNumber = generateUniqueCardNumber();
        Integer pin = generatePin();
        String encryptedPin = passwordEncoder.encode(pin.toString());

        Account account = Account.builder()
                .fullname(fullname)
                .email(email)
                .cardNumber(cardNumber)
                .balance(BigDecimal.ZERO)
                .deposit(BigDecimal.ZERO)
                .pin(encryptedPin)
                .build();
        accountRepository.save(account);
        account.setPin(pin.toString());
        return account;
    }
    public boolean checkPin(String enteredPin, String storedEncryptedPin) {
        return passwordEncoder.matches(enteredPin, storedEncryptedPin);
    }
}
