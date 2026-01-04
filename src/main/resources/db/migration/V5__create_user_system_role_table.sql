CREATE TABLE user_system_role (
    id               BIGSERIAL PRIMARY KEY,
    user_system_id   BIGINT NOT NULL,
    system_role_id   BIGINT NOT NULL,

    CONSTRAINT fk_usr_user_system
        FOREIGN KEY (user_system_id)
        REFERENCES user_system(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usr_system_role
        FOREIGN KEY (system_role_id)
        REFERENCES system_role(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_user_system_role UNIQUE (user_system_id, system_role_id)
);

CREATE INDEX idx_usr_user_system_id ON user_system_role(user_system_id);
CREATE INDEX idx_usr_system_role_id ON user_system_role(system_role_id);
