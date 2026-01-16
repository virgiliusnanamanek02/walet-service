package com.io.wallet_service.domain.model;

import com.io.wallet_service.application.dto_in.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String reference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected WalletTransaction() {}

    public WalletTransaction(
            UUID walletId,
            TransactionType type,
            BigDecimal amount,
            String reference
    ) {
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.reference = reference;
        this.createdAt = LocalDateTime.now();
    }

    public static WalletTransaction debit(
            Wallet wallet,
            BigDecimal amount,
            String reference
    ) {
        return new WalletTransaction(
                wallet.getId(),
                TransactionType.DEBIT,
                amount,
                reference
        );
    }

    public static WalletTransaction credit(
            Wallet wallet,
            BigDecimal amount,
            String reference
    ) {
        return new WalletTransaction(
                wallet.getId(),
                TransactionType.CREDIT,
                amount,
                reference
        );
    }
    public BigDecimal signedAmount() {
        return type == TransactionType.CREDIT
                ? amount
                : amount.negate();
    }
}

