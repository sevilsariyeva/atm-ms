package com.example.atmbackend_ms.exception;

import com.example.atmbackend_ms.util.HttpResponseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmailSendException(AccountNotFoundException ex, WebRequest request) {
        logger.info("AccountNotFoundException: {}", ex.getMessage());
        return ofType(request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String,Object>> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request){
        logger.info("InsufficientBalanceException: {}", ex.getMessage());
        return ofType(request,HttpStatus.BAD_REQUEST,ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request){
//        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    private ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message, List<?> validationErrors) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put(HttpResponseConstants.STATUS, status.value());
        attributes.put(HttpResponseConstants.ERROR, status.getReasonPhrase());
        attributes.put(HttpResponseConstants.MESSAGE, message);
        attributes.put(HttpResponseConstants.ERRORS, validationErrors);
        attributes.put(HttpResponseConstants.PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }

    private ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message) {
        return ofType(request, status, message, Collections.emptyList());
    }
}
