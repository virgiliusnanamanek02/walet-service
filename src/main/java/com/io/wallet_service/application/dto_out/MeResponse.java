package com.io.wallet_service.application.dto_out;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeResponse(
    UUID id,
    String email,
    LocalDateTime createdAt
) {}

