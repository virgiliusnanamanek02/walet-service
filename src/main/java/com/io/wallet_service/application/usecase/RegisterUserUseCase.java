package com.io.wallet_service.application.usecase;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.io.wallet_service.application.exception.EmailAlreadyExistsException;
import com.io.wallet_service.domain.model.User;
import com.io.wallet_service.domain.repository.UserRepository;

@Service
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional

    public UUID register(String email, String rawPassword){
        if (userRepository.existsByEmailAndDeletedFalse(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User(email, hashedPassword);
        userRepository.save(user);

        return user.getId();
    }


}
