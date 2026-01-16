package com.io.wallet_service.infrastructure.web;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.io.wallet_service.application.dto_out.MeResponse;
import com.io.wallet_service.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new MeResponse(
            user.getId(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}

