package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.exception.InsufficientBalanceException;
import com.example.atmbackend_ms.model.Transaction;
import com.example.atmbackend_ms.model.enums.TransactionType;
import com.example.atmbackend_ms.repository.AccountRepository;
import com.example.atmbackend_ms.repository.TransactionRepository;
import com.example.atmbackend_ms.util.HttpResponseConstants;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Service
public class WithdrawService extends BaseTransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AtmService.class);

    @Override
    @Transactional
    public String executeTransaction(String cardNumber, BigDecimal amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    if (account.getBalance().compareTo(amount)>=0) {
                        account.setBalance(account.getBalance().subtract(amount));
                        accountRepository.save(account);
                        logger.info(HttpResponseConstants.WITHDRAW_SUCCESS);
                        saveTransaction(cardNumber, TransactionType.WITHDRAW,amount, account.getBalance());
                        //emailService.sendEmail(account.getEmail(), HttpResponseConstants.WITHDRAW_SUCCESS, "You have withdrawn "+amount);
                        return HttpResponseConstants.WITHDRAW_SUCCESS+". New balance: " + account.getBalance();
                    }
                    throw new InsufficientBalanceException(HttpResponseConstants.BALANCE_EX);
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }
    private <T extends Enum<T>>void saveTransaction(String fromCardNumber, T type, BigDecimal amount, BigDecimal balanceAfterTransaction){
        transactionRepository.save(Transaction.builder()
                .fromCardNumber(fromCardNumber)
                .timestamp(LocalDateTime.now())
                .type(type)
                .amount(amount)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build());
    }
}
