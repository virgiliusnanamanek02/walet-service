CREATE TABLE IF NOT EXISTS wallet_transactions (
    id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    type VARCHAR(10) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    reference VARCHAR(100),
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_tx_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);

CREATE INDEX idx_wallet_tx_wallet_id ON wallet_transactions(wallet_id);
