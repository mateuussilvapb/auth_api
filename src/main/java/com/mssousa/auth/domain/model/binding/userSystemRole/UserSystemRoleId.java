package com.mssousa.auth.domain.model.binding.userSystemRole;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.DomainId;

/**
 * Value Object representando o identificador único de um vínculo usuário-perfil.
 * Garante que o ID seja válido conforme regras de negócio.
 */
public final class UserSystemRoleId extends DomainId {
 
    private UserSystemRoleId(Long value) {
        super(value);
        validate(value);
    }

    public static UserSystemRoleId of(Long value) {
        return new UserSystemRoleId(value);
    }

    private void validate(Long value) {
        if (value <= 0) {
            throw new DomainException("UserSystemRoleId deve ser um número positivo");
        }
    }
}
