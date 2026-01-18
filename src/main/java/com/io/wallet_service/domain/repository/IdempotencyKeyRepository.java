package com.io.wallet_service.domain.repository;

import com.io.wallet_service.domain.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {
    Optional<IdempotencyKey> findByUserIdAndIdemKeyAndEndpoint(
            UUID userId,
            String idemKey,
            String endpoint
    );
}
