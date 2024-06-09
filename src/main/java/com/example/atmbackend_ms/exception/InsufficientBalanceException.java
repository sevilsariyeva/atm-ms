package com.example.atmbackend_ms.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message){
        super(message);
    }
}
