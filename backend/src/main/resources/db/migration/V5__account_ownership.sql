ALTER TABLE accounts ADD COLUMN owner_username VARCHAR(80) NOT NULL DEFAULT 'demo';
CREATE INDEX idx_accounts_owner ON accounts(owner_username);
