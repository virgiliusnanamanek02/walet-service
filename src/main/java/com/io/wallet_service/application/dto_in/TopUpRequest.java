package com.io.wallet_service.application.dto_in;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TopUpRequest(@NotBlank BigDecimal amount, @NotBlank String reference) { }
