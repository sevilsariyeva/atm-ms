package com.example.atmbackend_ms.factory;

import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.model.Transaction;
import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.repository.AccountRepository;
import com.example.atmbackend_ms.repository.TransactionRepository;
import com.example.atmbackend_ms.service.AtmService;
import com.example.atmbackend_ms.util.HttpResponseConstants;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DepositService extends TransactionService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(AtmService.class);
    @Transactional
    public String deposit(String cardNumber, BigDecimal amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    accountRepository.save(account);
                    logger.info(HttpResponseConstants.DEPOSIT_SUCCESS);
                    saveTransaction(null, cardNumber, TransactionType.DEPOSIT, amount, account.getBalance());
                    return HttpResponseConstants.DEPOSIT_SUCCESS+". New balance: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }

    private void saveTransaction(String fromCardNumber,String toCardNumber, TransactionType type, BigDecimal amount, BigDecimal balanceAfterTransaction){
        transactionRepository.save(Transaction.builder()
                .fromCardNumber(fromCardNumber)
                .toCardNumber(toCardNumber)
                .timestamp(LocalDateTime.now())
                .type(type)
                .amount(amount)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build());
    }
}
