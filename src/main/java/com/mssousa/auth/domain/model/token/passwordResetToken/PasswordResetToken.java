package com.mssousa.auth.domain.model.token.passwordResetToken;

import java.time.Instant;
import java.util.Objects;
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
    private final PasswordResetTokenId id;
    private final ResetTokenValue value;
    private final UserId userId;
    private final Instant expiresAt;
    private Boolean used;

    /**
     * Construtor de Reconstituição (Persistence/Frameworks).
     * Não gera novos códigos e não valida regras de tempo (expiração), 
     * permitindo carregar estados passados do banco.
     */
    public PasswordResetToken(PasswordResetTokenId id, ResetTokenValue value, UserId userId, Instant expiresAt,
            Boolean used) {
        this.id = id;
        this.value = value;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.used = used;

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
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            throw new DomainException("Data de expiração inválida para novos PasswordResetToken");
        }

        return new PasswordResetToken(
            id,
            new ResetTokenValue(UUID.randomUUID().toString()),
            userId,
            expiresAt,
            false
        );
    }

    // ==================== Validações ====================

    /**
     * Valida apenas a integridade estrutural do objeto.
     */
    private void validateInvariants() {
        if (id == null) {
            throw new DomainException("ID é obrigatório para PasswordResetToken");
        }
        if (value == null) {
            throw new DomainException("Value é obrigatório para PasswordResetToken");
        }
        if (userId == null) {
            throw new DomainException("UserId é obrigatório para PasswordResetToken");
        }
        if (expiresAt == null) {
            throw new DomainException("Data de expiração é obrigatória");
        }
        if (used == null) {
            throw new DomainException("Status 'used' não pode ser nulo"); 
        }
    }

    /**
     * Valida se o token pode ser utilizado.
     */
    public void validateUsable() {
        if (isExpired()) {
            throw new DomainException("PasswordResetToken expirado");
        }
        if (isUsed()) {
            throw new DomainException("PasswordResetToken já foi utilizado");
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
}
