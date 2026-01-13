package com.mssousa.auth.domain.model.token.authorizationCode;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.User;

/**
 * Entidade de domínio representando um Código de autorização para solicitação de token.
 * <p>
 * Um AuthorizationCode é um token temporário que é gerado quando um usuário solicita
 * a autorização de um sistema cliente para acessar os recursos de um usuário.
 * </p>
 */
public class AuthorizationCode {
    private final AuthorizationCodeId id;
    private final String code;
    private final User user;
    private final SystemId systemId;

    private final Instant expiresAt;
    private Boolean used;

    /**
     * Construtor de Reconstituição (Persistence/Frameworks).
     * Não gera novos códigos e não valida regras de tempo (expiração), 
     * permitindo carregar estados passados do banco.
     */
    public AuthorizationCode(
            AuthorizationCodeId id,
            String code,
            User user,
            SystemId systemId,
            Instant expiresAt,
            Boolean used
    ) {
        this.id = id;
        this.code = code;
        this.user = user;
        this.systemId = systemId;
        this.expiresAt = expiresAt;
        this.used = used;
        
        validateInvariants();
    }

    /**
     * Factory Method para criar um NOVO AuthorizationCode.
     * Aplica regras de negócio de criação (ex: geração de UUID, validade futura).
     */
    public static AuthorizationCode create(
            AuthorizationCodeId id,
            User user,
            SystemId systemId,
            Instant expiresAt
    ) {
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            throw new DomainException("Data de expiração inválida para novos AuthorizationCode");
        }

        return new AuthorizationCode(
            id,
            UUID.randomUUID().toString(),
            user,
            systemId,
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
            throw new DomainException("ID é obrigatório para AuthorizationCode");
        }
        if (user == null) {
            throw new DomainException("Usuário é obrigatório para AuthorizationCode");
        }
        if (systemId == null) {
            throw new DomainException("O ID do sistema é obrigatório para AuthorizationCode");
        }
        if (code == null || code.trim().isEmpty()) {
             throw new DomainException("Code é obrigatório");
        }
        if (expiresAt == null) { // Valida apenas presença, não se está no passado
            throw new DomainException("Data de expiração é obrigatória");
        }
    }

    /**
     * Valida se o code pode ser utilizado.
     */
    public void validateUsable() {
        if (isExpired()) {
            throw new DomainException("AuthorizationCode expirado");
        }
        if (isUsed()) {
            throw new DomainException("AuthorizationCode já foi utilizado");
        }
    }

    // ==================== Métodos ====================

    /**
     * Marca o code como utilizado.
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

    public boolean belongsTo(SystemId systemId) {
        return Objects.equals(this.systemId, systemId);
    }

    // ==================== Getters ====================
    public AuthorizationCodeId getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public SystemId getSystemId() {
        return systemId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean getUsed() {
        return used;
    }
}
