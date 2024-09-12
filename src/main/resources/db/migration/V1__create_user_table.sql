CREATE TABLE users (
    id VARBINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    role TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
