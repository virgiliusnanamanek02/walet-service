package com.io.wallet_service.application.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(){
        super("insufficient balance");
    }
}
