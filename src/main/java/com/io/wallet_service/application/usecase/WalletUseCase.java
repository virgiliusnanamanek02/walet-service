package com.io.wallet_service.application.usecase;

import com.io.wallet_service.application.dto_in.TransactionType;
import com.io.wallet_service.domain.model.Wallet;
import com.io.wallet_service.domain.model.WalletTransaction;
import com.io.wallet_service.domain.repository.WalletRepository;
import com.io.wallet_service.domain.repository.WalletTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.StandardCharsets;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletUseCase {
	private final WalletRepository walletRepository;
	private final WalletTransactionRepository walletTransactionRepository;
	private final IdempotencyUseCase idempotencyUseCase;

	public WalletUseCase(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository,
			IdempotencyUseCase idempotencyUseCase) {
		this.walletRepository = walletRepository;
		this.walletTransactionRepository = walletTransactionRepository;
		this.idempotencyUseCase = idempotencyUseCase;
	}

	@Transactional
	public void credit(UUID walletId, BigDecimal amount, String reference) {
		Wallet wallet = walletRepository.findByUserId(walletId).orElseThrow();

		WalletTransaction walletTransaction = new WalletTransaction(wallet.getId(), TransactionType.CREDIT, amount,
				reference);

		walletTransactionRepository.save(walletTransaction);
	}

	@Transactional
	public void debit(UUID walletId, BigDecimal amount, String reference) {
		BigDecimal balance = walletTransactionRepository.calculateBalance(walletId);

		if (balance.compareTo(amount) < 0) {
			throw new IllegalStateException("Insufficient balance");
		}

		WalletTransaction walletTransaction = new WalletTransaction(walletId, TransactionType.DEBIT, amount, reference);

		walletTransactionRepository.save(walletTransaction);
	}

	@Transactional
	public void transfer(UUID fromWalletId, UUID toWalletId, BigDecimal amount, String reference) {

		Wallet sender = walletRepository.findByIdForUpdate(fromWalletId).orElseThrow();

		Wallet receiver = walletRepository.findByIdForUpdate(toWalletId).orElseThrow();

		if (sender.getId().equals(receiver.getId())) {
			throw new IllegalArgumentException("same wallet");
		}

		sender.debit(amount);
		receiver.credit(amount);

		walletTransactionRepository.save(WalletTransaction.debit(sender, amount, reference));

		walletTransactionRepository.save(WalletTransaction.credit(receiver, amount, reference));
	}

	@Transactional
	public void topUp(UUID userId, BigDecimal amount, String reference, String idemKey) {
		final String endpoint = "POST:/api/wallet/topup";

		String payload = String.join("|", userId.toString(), amount.stripTrailingZeros().toPlainString(), reference);

		String requestHash = DigestUtils.sha256Hex(payload.getBytes(StandardCharsets.UTF_8));

		idempotencyUseCase.validateAndStart(userId, idemKey, endpoint, requestHash);

		try {

			Wallet wallet = walletRepository.findByUserIdForUpdate(userId).orElseThrow();

			wallet.credit(amount);

			walletTransactionRepository.save(WalletTransaction.credit(wallet, amount, reference));

			idempotencyUseCase.markCompleted(userId, idemKey, endpoint);

		} catch (Exception e) {
			idempotencyUseCase.markCompleted(userId, idemKey, endpoint);
			throw e;
		}
	}

}
