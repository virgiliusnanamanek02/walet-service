package com.io.wallet_service.application.dto_out;

import java.util.UUID;

public class UserRegistrationResponse {
    private UUID userId;

    public UserRegistrationResponse(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
