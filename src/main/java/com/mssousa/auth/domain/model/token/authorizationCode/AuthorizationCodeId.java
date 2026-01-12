package com.mssousa.auth.domain.model.token.authorizationCode;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um AuthorizationCode.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public final class AuthorizationCodeId extends DomainId {

    public AuthorizationCodeId(Long value) {
        super(value);
        validate(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("AuthorizationCodeId deve ser um número positivo");
        }
    }
}
