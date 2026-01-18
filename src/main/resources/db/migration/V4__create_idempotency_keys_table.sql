CREATE TABLE IF NOT EXISTS idempotency_keys (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    idem_key VARCHAR(100) NOT NULL,
    endpoint VARCHAR(100) NOT NULL,
    request_hash VARCHAR(64) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    UNIQUE (user_id, idem_key, endpoint)
);
