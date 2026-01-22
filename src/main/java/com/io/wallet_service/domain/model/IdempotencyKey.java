package com.io.wallet_service.domain.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKey {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private UUID userId;
	private String idemKey;
	private String endpoint;
	private String requestHash;
	private String status;
	private Instant createdAt;

	protected IdempotencyKey() {
	}

	public static IdempotencyKey processing(UUID userId, String idemKey, String endpoint, String requestHash) {
		IdempotencyKey key = new IdempotencyKey();
		key.id = UUID.randomUUID();
		key.userId = userId;
		key.idemKey = idemKey;
		key.endpoint = endpoint;
		key.requestHash = requestHash;
		key.status = "PROCESSING";
		key.createdAt = Instant.now();
		return key;
	}

	public void markCompleted() {
		this.status = "COMPLETED";
	}

	public UUID getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getIdemKey() {
		return idemKey;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getRequestHash() {
		return requestHash;
	}

	public String getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
