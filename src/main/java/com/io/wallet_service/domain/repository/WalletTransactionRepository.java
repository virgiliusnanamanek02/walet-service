package com.io.wallet_service.domain.repository;

import com.io.wallet_service.domain.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    @Query("""
             SELECT COALESCE(SUM(
                        CASE
                            WHEN t.type = 'CREDIT' THEN t.amount
                            ELSE -t.amount
                        END
                    ), 0)
                    FROM WalletTransaction t
                    WHERE t.walletId = :walletId
            """)
    BigDecimal calculateBalance(UUID walletId);
}
