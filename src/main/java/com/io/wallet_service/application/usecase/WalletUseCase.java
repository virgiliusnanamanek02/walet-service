package com.io.wallet_service.application.usecase;

import com.io.wallet_service.application.dto_in.TransactionType;
import com.io.wallet_service.domain.model.Wallet;
import com.io.wallet_service.domain.model.WalletTransaction;
import com.io.wallet_service.domain.repository.WalletRepository;
import com.io.wallet_service.domain.repository.WalletTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletUseCase {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletUseCase(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Transactional
    public void credit(UUID walletId, BigDecimal amount, String reference){
        Wallet wallet = walletRepository.findByUserId(walletId).orElseThrow();

        WalletTransaction walletTransaction = new WalletTransaction(
                wallet.getId(), TransactionType.CREDIT, amount, reference
        );

        walletTransactionRepository.save(walletTransaction);
    }

    @Transactional
    public void debit(UUID walletId, BigDecimal amount, String reference){
        BigDecimal balance = walletTransactionRepository.calculateBalance(walletId);

        if (balance.compareTo(amount) < 0){
            throw new IllegalStateException("Insufficient balance");
        }

        WalletTransaction walletTransaction = new WalletTransaction(
                walletId, TransactionType.DEBIT, amount, reference
        );

        walletTransactionRepository.save(walletTransaction);
    }

    @Transactional
    public void transfer(
            UUID fromWalletId,
            UUID toWalletId,
            BigDecimal amount,
            String reference
    ) {

        Wallet sender = walletRepository
                .findByIdForUpdate(fromWalletId)
                .orElseThrow();

        Wallet receiver = walletRepository
                .findByIdForUpdate(toWalletId)
                .orElseThrow();

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("same wallet");
        }

        sender.debit(amount);
        receiver.credit(amount);

        walletTransactionRepository.save(
                WalletTransaction.debit(sender, amount, reference)
        );

        walletTransactionRepository.save(
                WalletTransaction.credit(receiver, amount, reference)
        );
    }

    @Transactional
    public void topUp(
            UUID userId,
            BigDecimal amount,
            String reference
    ) {
        Wallet wallet = walletRepository
                .findByUserIdForUpdate(userId)
                .orElseThrow();

        wallet.credit(amount);

        walletTransactionRepository.save(
                WalletTransaction.credit(wallet, amount, reference)
        );
    }
}
