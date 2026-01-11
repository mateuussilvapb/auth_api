package com.mssousa.auth.domain.model.binding.userSystem;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um vínculo usuário-sistema.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public class UserSystemId extends DomainId {
 
    public UserSystemId(Long value) {
        super(value);
        validate(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("UserSystemId deve ser um número positivo");
        }
    }
}
