package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.exception.InsufficientBalanceException;
import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.model.Transaction;
import com.example.atmbackend_ms.repository.AccountRepository;
import com.example.atmbackend_ms.repository.AtmRepository;
import com.example.atmbackend_ms.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AtmService implements AtmRepository {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(AtmService.class);

    @Override
    public String validatePin(String cardNumber, int pin) {
        return accountRepository.findByCardNumber(cardNumber)
                .filter(account->account.getPin()==pin)
                .map(account -> {
                    logger.info("PIN validation successful for card number: {}", cardNumber);
                    return "PIN validation successful";
                })
                .orElseThrow(() -> new AccountNotFoundException("Invalid PIN or Account not found"));
    }

    @Override
    public String checkBalance(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    logger.info("Balance check successful for card number: {}", cardNumber);
                    saveTransaction(cardNumber, "CHECK_BALANCE",0, account.getBalance());
                    return "Your balance is: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public String withDraw(String cardNumber, double amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    if (account.getBalance() >= amount) {
                        account.setBalance(account.getBalance() - amount);
                        accountRepository.save(account);
                        logger.info("Withdrawal successful for card number: {}", cardNumber);
                        saveTransaction(cardNumber, "WITHDRAW",amount, account.getBalance());
                        return "Withdrawal successful. New balance: " + account.getBalance();
                    } else {
                        throw new InsufficientBalanceException("Insufficient balance");
                    }
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public String deposit(String cardNumber, double amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    account.setBalance(account.getBalance() + amount);
                    accountRepository.save(account);
                    logger.info("Deposit successful for card number: {}", cardNumber);
                    saveTransaction(cardNumber, "WITHDRAW",amount, account.getBalance());
                    return "Deposit successful. New balance: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    private void saveTransaction(String cardNumber, String type, double amount, double balanceAfterTransaction){
        Transaction transaction=Transaction.builder()
                .cardNumber(cardNumber)
                .timestamp(LocalDateTime.now())
                .type(type)
                .amount(amount)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build();
        transactionRepository.save(transaction);
    }
}
