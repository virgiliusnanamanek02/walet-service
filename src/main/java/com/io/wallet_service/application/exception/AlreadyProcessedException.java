package com.io.wallet_service.application.exception;

public class AlreadyProcessedException extends RuntimeException{
    public AlreadyProcessedException() {
        super("Request already processed");
    }
}
