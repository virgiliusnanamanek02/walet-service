CREATE TABLE  IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Email harus unik (identity utama user)
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_email ON users (email);

-- Index untuk soft delete query
CREATE INDEX IF NOT EXISTS ix_users_deleted ON users (deleted);
