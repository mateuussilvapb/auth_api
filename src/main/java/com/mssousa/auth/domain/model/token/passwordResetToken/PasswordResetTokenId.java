package com.mssousa.auth.domain.model.token.passwordResetToken;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um PasswordResetToken.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public class PasswordResetTokenId extends DomainId {
 
    public PasswordResetTokenId(Long value) {
        super(value);
        validate(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("PasswordResetTokenId deve ser um número positivo");
        }
    }
}
