package com.mssousa.auth.domain.model.token.passwordResetToken;

import java.time.Instant;
import java.util.UUID;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.user.UserId;

/**
 * Entidade de domínio representando um token de redefinição de senha.
 * <p>
 * Um PasswordResetToken é um token temporário que é gerado quando um usuário
 * solicita
 * a redefinição de senha.
 * </p>
 */
public class PasswordResetToken {
    public static final String ERROR_ID_REQUIRED = "ID é obrigatório para PasswordResetToken";
    public static final String ERROR_VALUE_REQUIRED = "Value é obrigatório para PasswordResetToken";
    public static final String ERROR_USER_ID_REQUIRED = "UserId é obrigatório para PasswordResetToken";
    public static final String ERROR_EXPIRES_AT_REQUIRED = "Data de expiração é obrigatória";
    public static final String ERROR_USED_STATUS_REQUIRED = "Status 'used' não pode ser nulo";
    public static final String ERROR_EXPIRED_TOKEN = "PasswordResetToken expirado";
    public static final String ERROR_TOKEN_ALREADY_USED = "PasswordResetToken já foi utilizado";
    public static final String ERROR_EXPIRATION_MUST_BE_FUTURE = "Data de expiração inválida para novos PasswordResetToken. Deve ser futura.";

    private final PasswordResetTokenId id;
    private final ResetTokenValue value;
    private final UserId userId;
    private final Instant expiresAt;
    private Boolean used;

    /**
     * Construtor privado para garantir uso via Builder.
     * 
     * @param builder Builder do PasswordResetToken
     */
    private PasswordResetToken(Builder builder) {
        this.id = builder.id;
        this.value = builder.value;
        this.userId = builder.userId;
        this.expiresAt = builder.expiresAt;
        this.used = builder.used;

        validateInvariants();
    }

    /**
     * Factory Method para criar um NOVO PasswordResetToken.
     * Aplica regras de negócio de criação.
     */
    public static PasswordResetToken create(
            PasswordResetTokenId id,
            UserId userId,
            Instant expiresAt
    ) {
        if (expiresAt == null) {
            throw new DomainException(ERROR_EXPIRES_AT_REQUIRED);
        }
        if (expiresAt.isBefore(Instant.now())) {
            throw new DomainException(ERROR_EXPIRATION_MUST_BE_FUTURE);
        }

        return new Builder()
            .id(id)
            .value(new ResetTokenValue(UUID.randomUUID().toString()))
            .userId(userId)
            .expiresAt(expiresAt)
            .used(false)
            .build();
    }

    // ==================== Validações ====================

    /**
     * Valida apenas a integridade estrutural do objeto.
     */
    private void validateInvariants() {
        if (id == null) {
            throw new DomainException(ERROR_ID_REQUIRED);
        }
        if (value == null) {
            throw new DomainException(ERROR_VALUE_REQUIRED);
        }
        if (userId == null) {
            throw new DomainException(ERROR_USER_ID_REQUIRED);
        }
        if (expiresAt == null) {
            throw new DomainException(ERROR_EXPIRES_AT_REQUIRED);
        }
        if (used == null) {
            throw new DomainException(ERROR_USED_STATUS_REQUIRED);
        }
    }

    /**
     * Valida se o token pode ser utilizado.
     */
    public void validateUsable() {
        if (isExpired()) {
            throw new DomainException(ERROR_EXPIRED_TOKEN);
        }
        if (isUsed()) {
            throw new DomainException(ERROR_TOKEN_ALREADY_USED);
        }
    }

    // ==================== Métodos de Domínio ====================

    /**
     * Marca o token como utilizado.
     * Deve ser chamado exatamente uma vez.
     */
    public void markAsUsed() {
        validateUsable();
        this.used = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isUsed() {
        return Boolean.TRUE.equals(used);
    }

    // ==================== Getters ====================

    public PasswordResetTokenId getId() {
        return id;
    }

    public ResetTokenValue getValue() {
        return value;
    }

    public UserId getUserId() {
        return userId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Boolean getUsed() {
        return used;
    }

    // ==================== Builder ====================
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PasswordResetTokenId id;
        private ResetTokenValue value;
        private UserId userId;
        private Instant expiresAt;
        private Boolean used;

        public Builder id(PasswordResetTokenId id) {
            this.id = id;
            return this;
        }

        public Builder value(ResetTokenValue value) {
            this.value = value;
            return this;
        }

        public Builder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder used(Boolean used) {
            this.used = used;
            return this;
        }

        public PasswordResetToken build() {
            if (id == null) {
                throw new DomainException(ERROR_ID_REQUIRED);
            }
            if (value == null) {
                throw new DomainException(ERROR_VALUE_REQUIRED);
            }
            if (userId == null) {
                throw new DomainException(ERROR_USER_ID_REQUIRED);
            }
            if (expiresAt == null) {
                throw new DomainException(ERROR_EXPIRES_AT_REQUIRED);
            }
            if (used == null) {
                throw new DomainException(ERROR_USED_STATUS_REQUIRED);
            }
            return new PasswordResetToken(this);
        }
    }
}
