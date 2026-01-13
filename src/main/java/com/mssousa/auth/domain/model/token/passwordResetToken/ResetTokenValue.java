package com.mssousa.auth.domain.model.token.passwordResetToken;

import java.util.Objects;

/**
 * Value Object representando o valor de um token de redefinição de senha.
 * Garante que o valor seja válido conforme regras de negócio.
 */
public class ResetTokenValue {
     private final String value;

    public ResetTokenValue(String value) {
        validateInvariants(value);
        this.value = value;
    }

    /**
     * Valida o valor passado por parametro.
     * @param value
     */
    public void validateInvariants(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password reset token não pode ser nulo ou vazio");
        }

        if (value.length() < 32) {
            throw new IllegalArgumentException("Password reset token inválido");
        }
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResetTokenValue that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
