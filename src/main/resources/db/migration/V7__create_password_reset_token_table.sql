CREATE TABLE password_reset_token (
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL,
    user_id     BIGINT       NOT NULL,
    expires_at  TIMESTAMP    NOT NULL,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_password_reset_token UNIQUE (token),

    CONSTRAINT fk_prt_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_prt_expires_at ON password_reset_token(expires_at);
CREATE INDEX idx_prt_used ON password_reset_token(used);
