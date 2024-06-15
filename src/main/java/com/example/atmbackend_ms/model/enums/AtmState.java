package com.example.atmbackend_ms.model.enums;

public enum AtmState {
    READY,
    PIN,
    SELECT_TRANSACTION,
    DEPOSIT,
    WITHDRAW,
    DISPLAY_BALANCE,
    ERROR_MESSAGE_DISPLAYED,
    INVALID_CASH_RETURNED,
    EXIT_MESSAGE_DISPLAYED
}
