package com.example.atmbackend_ms.service;

import com.example.atmbackend_ms.context.AtmContext;
import com.example.atmbackend_ms.exception.AccountNotFoundException;
import com.example.atmbackend_ms.exception.InsufficientBalanceException;
import com.example.atmbackend_ms.exception.InvalidCardStateException;
import com.example.atmbackend_ms.exception.InvalidPinException;
import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.model.Transaction;
import com.example.atmbackend_ms.model.enums.AtmState;
import com.example.atmbackend_ms.model.enums.TransferType;
import com.example.atmbackend_ms.repository.AccountRepository;
import com.example.atmbackend_ms.repository.AtmRepository;
import com.example.atmbackend_ms.repository.TransactionRepository;
import com.example.atmbackend_ms.util.HttpResponseConstants;
import com.example.atmbackend_ms.model.enums.TransactionType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AtmService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AtmRepository atmRepository;
    private final EmailService emailService;
    private final AtmContext atmContext;

    @Autowired
    public AtmService(AccountRepository accountRepository,
                      TransactionRepository transactionRepository,
                      AtmRepository atmRepository,
                      EmailService emailService,
                      AtmContext atmContext) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.atmRepository = atmRepository;
        this.emailService = emailService;
        this.atmContext = atmContext;
    }
    private static final Logger logger = LoggerFactory.getLogger(AtmService.class);

    public String insertCard(String cardNumber){
        return Optional.ofNullable(atmContext.getAtmState())
                .filter(state->state.equals(AtmState.READY))
                .map(state->{AtmContext.builder()
                        .cardNumber(cardNumber)
                        .atmState(AtmState.PIN).build();
                    return HttpResponseConstants.INSERT_SUCCESS;})
                .orElseThrow(()->new InvalidCardStateException(HttpResponseConstants.INSERT_EX));
    }
    public Account checkCardNumberexists(String cardNumber){
        return accountRepository.findByCardNumber(cardNumber).orElseThrow(()->new AccountNotFoundException("Account not found"));
    }
    public String validatePin(String cardNumber, Integer pin) {
        String pinStr = pin != null ? pin.toString() : "";

        if (pinStr.length() != 4) {
            throw new InvalidPinException("Please enter a valid 4-digit PIN.");
        }

        return accountRepository.findByCardNumber(cardNumber)
                .filter(account -> account.getPin().equals(pin))
                .map(account -> {
                    logger.info(HttpResponseConstants.PIN_SUCCESS);
                    return HttpResponseConstants.PIN_SUCCESS;
                })
                .orElseThrow(() -> new InvalidPinException(HttpResponseConstants.INVALID_PIN_EX));
    }


    public String checkBalance(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    logger.info(HttpResponseConstants.BALANCE_SUCCESS);
                    saveTransaction(null, cardNumber, TransferType.CHECK_BALANCE,null, account.getBalance());
                    return HttpResponseConstants.BALANCE_SUCCESS+". Your balance is: " + account.getBalance();
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }

    @Transactional
    public String withDraw(String cardNumber, BigDecimal amount) {
        return accountRepository.findByCardNumber(cardNumber)
                .map(account -> {
                    if (account.getBalance().compareTo(amount)>=0) {
                        account.setBalance(account.getBalance().subtract(amount));
                        accountRepository.save(account);
                        logger.info(HttpResponseConstants.WITHDRAW_SUCCESS);
                        saveTransaction(cardNumber, null, TransactionType.WITHDRAW,amount, account.getBalance());
                        //emailService.sendEmail(account.getEmail(), HttpResponseConstants.WITHDRAW_SUCCESS, "You have withdrawn "+amount);
                        return HttpResponseConstants.WITHDRAW_SUCCESS+". New balance: " + account.getBalance();
                    }
                        throw new InsufficientBalanceException(HttpResponseConstants.BALANCE_EX);
                })
                .orElseThrow(() -> new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }
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
    @Transactional
    public String transfer(String fromCardNumber, String toCardNumber, BigDecimal amount){
        Account fromAccount=getAccountByCardNumber(fromCardNumber);
        Account toAccount=getAccountByCardNumber(toCardNumber);
        if(fromAccount.getBalance().compareTo(amount)<0){
            throw new InsufficientBalanceException(HttpResponseConstants.BALANCE_EX);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        saveTransaction(fromCardNumber,toCardNumber, TransferType.TRANSFER_OUT, amount,fromAccount.getBalance());
        saveTransaction(toCardNumber,toCardNumber,TransferType.TRANSFER_IN, amount,toAccount.getBalance());

        logger.info(HttpResponseConstants.TRANSFER_SUCCESS);

        return HttpResponseConstants.TRANSFER_SUCCESS;
    }

    private <T extends Enum<T>>void saveTransaction(String fromCardNumber,String toCardNumber, T type, BigDecimal amount, BigDecimal balanceAfterTransaction){
        transactionRepository.save(Transaction.builder()
                .fromCardNumber(fromCardNumber)
                .toCardNumber(toCardNumber)
                .timestamp(LocalDateTime.now())
                .type(type)
                .amount(amount)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build());
    }

    private Account getAccountByCardNumber(String cardNumber){
        return accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(()->new AccountNotFoundException(HttpResponseConstants.ACCOUNT_EX));
    }
}
