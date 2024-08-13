package com.example.atmbackend_ms.controller;

import com.example.atmbackend_ms.service.AccountService;
import com.example.atmbackend_ms.service.AtmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AtmService atmService;


}
