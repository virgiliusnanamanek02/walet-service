package com.io.wallet_service.application.usecase;

import com.io.wallet_service.application.exception.AlreadyProcessedException;
import com.io.wallet_service.domain.model.IdempotencyKey;
import com.io.wallet_service.domain.repository.IdempotencyKeyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdempotencyUseCase {
    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyUseCase(IdempotencyKeyRepository idempotencyKeyRepository) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
    }

    @Transactional
    public void validateAndStart( UUID userId, String idemKey, String endpoint, String requestHash){
        idempotencyKeyRepository.findByUserIdAndIdemKeyAndEndpoint(userId, idemKey, endpoint)
                .ifPresent(existing -> {
                    if (!existing.getRequestHash().equals(requestHash)){
                        throw new IllegalStateException(
                                "Idempotency-Key reuse with different payload"
                        );
                    }

                    if ("COMPLETED".equals(existing.getStatus())) {
                        throw new AlreadyProcessedException();
                    }
                });

        idempotencyKeyRepository.save(
                IdempotencyKey.processing(
                        userId, idemKey, endpoint, requestHash
                )
        );
    }

    @Transactional
    public void markCompleted(
            UUID userId,
            String idemKey,
            String endpoint
    ) {
        IdempotencyKey key = idempotencyKeyRepository
                .findByUserIdAndIdemKeyAndEndpoint(userId, idemKey, endpoint)
                .orElseThrow();

        key.markCompleted();
    }
}
