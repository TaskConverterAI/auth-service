CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255)            NOT NULL UNIQUE,
    email      VARCHAR(255)            NOT NULL UNIQUE,
    password   VARCHAR(255)            NOT NULL,
    role       VARCHAR(50)             NOT NULL,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE INDEX idx_users_username ON users (username);

CREATE TABLE sessions
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT                  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    refresh_token VARCHAR(255)            NOT NULL UNIQUE,
    is_active     BOOLEAN                 NOT NULL DEFAULT TRUE,
    expires_at    TIMESTAMP               NOT NULL,
    created_at    TIMESTAMP DEFAULT now() NOT NULL,
    updated_at    TIMESTAMP DEFAULT now() NOT NULL
);

CREATE INDEX idx_sessions_user_id ON sessions (user_id);
CREATE INDEX idx_sessions_refresh_token ON sessions (refresh_token);
CREATE INDEX idx_sessions_expires_at ON sessions (expires_at);
