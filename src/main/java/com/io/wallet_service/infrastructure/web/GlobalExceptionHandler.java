package com.io.wallet_service.infrastructure.web;

import com.io.wallet_service.application.exception.AlreadyProcessedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyProcessedException.class)
    public ResponseEntity<?> handleAlreadyProcessed(
            AlreadyProcessedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        Map.of(
                                "status", "ALREADY_PROCESSED",
                                "message", ex.getMessage()
                        )
                );
    }
}
