CREATE TABLE system_role (
    id           BIGSERIAL PRIMARY KEY,
    system_id    BIGINT       NOT NULL,
    code         VARCHAR(50)  NOT NULL,
    description  VARCHAR(255),
    status       VARCHAR(20)  NOT NULL,

    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(50),

    CONSTRAINT fk_system_role_system
        FOREIGN KEY (system_id)
        REFERENCES client_system(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_system_role_code UNIQUE (system_id, code)
);

CREATE INDEX idx_system_role_system_id ON system_role(system_id);
CREATE INDEX idx_system_role_status ON system_role(status);
