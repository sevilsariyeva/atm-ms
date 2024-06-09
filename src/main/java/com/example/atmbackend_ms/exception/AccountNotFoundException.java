package com.example.atmbackend_ms.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message){
        super(message);
    }
}
