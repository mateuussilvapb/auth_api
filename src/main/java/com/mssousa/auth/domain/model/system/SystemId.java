package com.mssousa.auth.domain.model.system;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um sistema cliente.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public final class SystemId extends DomainId {

    private SystemId(Long value) {
        super(value);
        validate(value);
    }

    public static SystemId of(Long value) {
        return new SystemId(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("SystemId deve ser um número positivo");
        }
    }
}
