package com.io.wallet_service.application.dto_in;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotBlank UUID fromWalletId,
        @NotBlank UUID toWalletId,
        @NotBlank BigDecimal amount,
        @NotBlank String reference
) {}
