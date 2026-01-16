package com.io.wallet_service.infrastructure.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.io.wallet_service.application.dto_in.LoginRequest;
import com.io.wallet_service.application.dto_in.UserRegistrationRequest;
import com.io.wallet_service.application.dto_out.LoginResponse;
import com.io.wallet_service.application.dto_out.UserRegistrationResponse;
import com.io.wallet_service.application.usecase.AuthUserUseCase;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthUserUseCase authService;

    public AuthController(AuthUserUseCase authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> postMethodName(@RequestBody @Valid UserRegistrationRequest request) {
        
        UUID userId = authService.register(request.getEmail(), request.getPassword());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserRegistrationResponse(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    
}
