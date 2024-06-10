package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.exception.InsufficientBalanceException;
import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.model.Transaction;
import com.example.atmbackend_ms.repository.AccountRepository;
import com.example.atmbackend_ms.repository.AtmRepository;
import com.example.atmbackend_ms.repository.TransactionRepository;
import com.example.atmbackend_ms.util.HttpResponseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AtmService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AtmRepository atmRepository;
    @Autowired
    private EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AtmService.class);

    public String validatePin(String cardNumber, Integer pin) {
        return accountRepository.findByCardNumber(cardNumber)
                .filter(account->account.getPin().equals(pin))
                .map(account -> {
                    logger.info(HttpResponseConstants.PIN_SUCCESS);
                    return HttpResponseConstants.PIN_SUCCESS;
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.PIN_OR_ACCOUNT_EX));
    }

    public String checkBalance(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    logger.info(HttpResponseConstants.BALANCE_SUCCESS);
                    saveTransaction(cardNumber, "CHECK_BALANCE",null, account.getBalance());
                    return HttpResponseConstants.BALANCE_SUCCESS+". Your balance is: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }

    public String withDraw(String cardNumber, BigDecimal amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    if (account.getBalance().compareTo(amount)>=0) {
                        account.setBalance(account.getBalance().subtract(amount));
                        accountRepository.save(account);
                        logger.info(HttpResponseConstants.WITHDRAW_SUCCESS);
                        saveTransaction(cardNumber, "WITHDRAW",amount, account.getBalance());
                        //emailService.sendEmail(account.getEmail(), HttpResponseConstants.WITHDRAW_SUCCESS, "You have withdrawn "+amount);
                        return HttpResponseConstants.WITHDRAW_SUCCESS+". New balance: " + account.getBalance();
                    }
                        throw new InsufficientBalanceException(HttpResponseConstants.BALANCE_EX);
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }

    public String deposit(String cardNumber, BigDecimal amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    account.setBalance(account.getBalance().add(amount));
                    accountRepository.save(account);
                    logger.info(HttpResponseConstants.DEPOSIT_SUCCESS);
                    saveTransaction(cardNumber, "WITHDRAW",amount, account.getBalance());
                    return HttpResponseConstants.DEPOSIT_SUCCESS+". New balance: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }

    public String transfer(String fromCardNumber, String toCardNumber, BigDecimal amount){
        Account fromAccount=accountRepository.findByCardNumber(fromCardNumber)
                .orElseThrow(()->new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
        Account toAccount=accountRepository.findByCardNumber(toCardNumber)
                .orElseThrow(()->new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
        if(fromAccount.getBalance().compareTo(toAccount.getBalance())<0){
            throw new InsufficientBalanceException(HttpResponseConstants.BALANCE_EX);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        saveTransaction(fromCardNumber,"TRANSFER_OUT",amount,fromAccount.getBalance());
        saveTransaction(toCardNumber,"TRANSFER_IN",amount,toAccount.getBalance());

        logger.info(HttpResponseConstants.TRANSFER_SUCCESS);

        return HttpResponseConstants.TRANSFER_SUCCESS;
    }

    private void saveTransaction(String cardNumber, String type, BigDecimal amount, BigDecimal balanceAfterTransaction){
        transactionRepository.save(Transaction.builder()
                .cardNumber(cardNumber)
                .timestamp(LocalDateTime.now())
                .type(type)
                .amount(amount)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build());
    }
}
