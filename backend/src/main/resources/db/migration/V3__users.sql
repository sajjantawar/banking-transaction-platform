CREATE TABLE app_users (
 id UUID PRIMARY KEY,
 username VARCHAR(80) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role VARCHAR(30) NOT NULL
);
INSERT INTO app_users(id,username,password_hash,role) VALUES ('00000000-0000-0000-0000-000000000001','demo','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy','CUSTOMER');
