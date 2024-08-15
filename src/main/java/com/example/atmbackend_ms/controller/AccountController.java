package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.dto.RegisterRequest;
import com.example.atmbackend_ms.model.Account;
import com.example.atmbackend_ms.service.AccountService;
import com.example.atmbackend_ms.service.AtmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public Account registerAccount(@RequestBody RegisterRequest request) {
        return accountService.registerAccount(request.getFullname(), request.getEmail());
    }
}
