package com.io.wallet_service.application.dto_in;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String email,
    @NotBlank String password
) {}

