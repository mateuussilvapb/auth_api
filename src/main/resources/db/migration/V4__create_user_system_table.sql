CREATE TABLE user_system (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT      NOT NULL,
    system_id   BIGINT      NOT NULL,
    status      VARCHAR(20) NOT NULL,

    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by  VARCHAR(50),

    CONSTRAINT fk_user_system_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_system_system
        FOREIGN KEY (system_id)
        REFERENCES client_system(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_user_system UNIQUE (user_id, system_id)
);

CREATE INDEX idx_user_system_user_id ON user_system(user_id);
CREATE INDEX idx_user_system_system_id ON user_system(system_id);
CREATE INDEX idx_user_system_status ON user_system(status);
