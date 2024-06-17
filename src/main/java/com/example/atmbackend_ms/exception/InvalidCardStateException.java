package com.example.atmbackend_ms.exception;

public class InvalidCardStateException extends RuntimeException{
    public InvalidCardStateException(String message){
        super(message);
    }
}
