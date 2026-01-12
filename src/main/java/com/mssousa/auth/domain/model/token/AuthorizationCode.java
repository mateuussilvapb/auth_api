package com.mssousa.auth.domain.model.token;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.user.User;

public class AuthorizationCode {
    private final AuthorizationCodeId id;
    private final String code;
    private final User user;
    private final ClientSystem system;

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
            ClientSystem system,
            Instant expiresAt,
            Boolean used
    ) {
        this.id = id;
        this.code = code;
        this.user = user;
        this.system = system;
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
            ClientSystem system,
            Instant expiresAt
    ) {
        if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
            throw new DomainException("Data de expiração inválida para novos AuthorizationCode");
        }

        return new AuthorizationCode(
            id,
            UUID.randomUUID().toString(),
            user,
            system,
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
        if (system == null) {
            throw new DomainException("Sistema é obrigatório para AuthorizationCode");
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
        return used == true;
    }

    public boolean belongsTo(ClientSystem system) {
        return Objects.equals(this.system, system);
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

    public ClientSystem getSystem() {
        return system;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean getUsed() {
        return used;
    }
}
