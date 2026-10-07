CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    account_number VARCHAR(32) NOT NULL UNIQUE,
    account_holder_name VARCHAR(120) NOT NULL,
    balance NUMERIC(19,4) NOT NULL CHECK (balance >= 0),
    version BIGINT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE transaction_records (
    id UUID PRIMARY KEY,
    source_account VARCHAR(32) NOT NULL,
    destination_account VARCHAR(32) NOT NULL,
    amount NUMERIC(19,4) NOT NULL CHECK (amount > 0),
    currency VARCHAR(3) NOT NULL,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_transaction_source ON transaction_records(source_account);
CREATE INDEX idx_transaction_destination ON transaction_records(destination_account);
CREATE INDEX idx_transaction_created_at ON transaction_records(created_at);

INSERT INTO accounts (id, account_number, account_holder_name, balance, version, status)
VALUES
('00000000-0000-0000-0000-000000000001', 'ACC100001', 'Demo Customer One', 10000.0000, 0, 'ACTIVE'),
('00000000-0000-0000-0000-000000000002', 'ACC100002', 'Demo Customer Two', 5000.0000, 0, 'ACTIVE');
