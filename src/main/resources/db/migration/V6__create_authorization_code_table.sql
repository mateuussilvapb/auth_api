CREATE TABLE authorization_code (
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(255) NOT NULL,
    user_id     BIGINT       NOT NULL,
    system_id   BIGINT       NOT NULL,
    expires_at  TIMESTAMP    NOT NULL,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_authorization_code UNIQUE (code),

    CONSTRAINT fk_auth_code_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_auth_code_system
        FOREIGN KEY (system_id)
        REFERENCES client_system(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_auth_code_expires_at ON authorization_code(expires_at);
CREATE INDEX idx_auth_code_used ON authorization_code(used);
