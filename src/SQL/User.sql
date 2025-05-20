CREATE TABLE users (
    id bigint PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(50) DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role) VALUES (
  'admin',
  '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36N.ezCbXM1YjEoQ3Bkp9b6',  -- bcrypt encoded 'admin123'
  'ADMIN'
);