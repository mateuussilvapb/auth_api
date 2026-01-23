package com.mssousa.auth.domain.model.role;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um perfil.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public final class SystemRoleId extends DomainId {

    private SystemRoleId(Long value) {
        super(value);
        validate(value);
    }

    public static SystemRoleId of(Long value) {
        return new SystemRoleId(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("SystemRoleId deve ser um número positivo");
        }
    }
}
