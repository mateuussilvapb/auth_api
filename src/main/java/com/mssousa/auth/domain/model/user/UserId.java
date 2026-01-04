package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um usuário.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public final class UserId extends DomainId {

    public UserId(Long value) {
        super(value);
        validate(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("UserId deve ser um número positivo");
        }
    }
}
