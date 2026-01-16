package com.io.wallet_service.application.dto_out;

import java.time.Instant;

public record LoginResponse(
    String accessToken,
    Instant expiresAt
) {}

