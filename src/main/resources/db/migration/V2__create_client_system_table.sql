CREATE TABLE client_system (
    id              BIGSERIAL PRIMARY KEY,
    client_id       VARCHAR(100) NOT NULL,
    client_secret   VARCHAR(255),
    name            VARCHAR(150) NOT NULL,
    redirect_uri    VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL,

    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(50),

    CONSTRAINT uq_client_system_client_id UNIQUE (client_id)
);

CREATE INDEX idx_client_system_status ON client_system(status);
