package com.io.wallet_service.application.dto_out;

import java.util.UUID;

public class UserRegistrationResponse {
    private UUID userId;
    // private String email;

    public UserRegistrationResponse(UUID userId) {
        this.userId = userId;
        // this.email = email;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    // public String getEmail() {
    //     return this.email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

}
