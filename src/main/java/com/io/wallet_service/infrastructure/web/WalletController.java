package com.io.wallet_service.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.io.wallet_service.application.dto_in.TopUpRequest;
import com.io.wallet_service.application.dto_in.TransferRequest;
import com.io.wallet_service.application.usecase.WalletUseCase;
import com.io.wallet_service.domain.model.User;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	private final WalletUseCase walletUseCase;

	public WalletController(WalletUseCase walletUseCase) {
		this.walletUseCase = walletUseCase;
	}

	@PostMapping("/topup")
	public ResponseEntity<Void> topUp(@AuthenticationPrincipal User user,
			@RequestHeader("Idempotency-Key") String idemKey, @RequestBody TopUpRequest request) {
		walletUseCase.topUp(user.getId(), request.amount(), request.reference(), idemKey);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/transfer")
	public ResponseEntity<Void> transfer(@AuthenticationPrincipal User user, @RequestBody TransferRequest request) {
		walletUseCase.transfer(request.fromWalletId(), request.toWalletId(), request.amount(), request.reference());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
