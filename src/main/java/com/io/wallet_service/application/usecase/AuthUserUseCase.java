package com.io.wallet_service.application.usecase;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.io.wallet_service.application.dto_in.LoginRequest;
import com.io.wallet_service.application.dto_out.LoginResponse;
import com.io.wallet_service.application.exception.EmailAlreadyExistsException;
import com.io.wallet_service.domain.model.User;
import com.io.wallet_service.domain.repository.UserRepository;
import com.io.wallet_service.infrastructure.security.JwtTokenProvider;

@Service
public class AuthUserUseCase {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public UUID register(String email, String rawPassword) {
		if (userRepository.existsByEmailAndDeletedFalse(email)) {
			throw new EmailAlreadyExistsException(email);
		}

		String hashedPassword = passwordEncoder.encode(rawPassword);

		User user = new User(email, hashedPassword);
		userRepository.save(user);

		return user.getId();
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {

		User user = userRepository.findByEmailAndDeletedFalse(request.email())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtTokenProvider.generateToken(user);

		return new LoginResponse(token, Instant.now().plusSeconds(900));
	}

}
