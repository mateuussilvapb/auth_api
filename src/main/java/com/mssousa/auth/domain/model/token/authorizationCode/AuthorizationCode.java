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
    // Mensagens de erro para validações
    public static final String ID_NULL = "Authorization Code ID não pode ser nulo";
    public static final String CODE_NULL_OR_BLANK = "Authorization Code code não pode ser nulo ou vazio";
    public static final String USER_NULL = "User não pode ser nulo";
    public static final String SYSTEM_ID_NULL = "System ID não pode ser nulo";
    public static final String EXPIRES_AT_NULL = "Expires At não pode ser nulo";
    public static final String EXPIRES_AT_PAST = "Expires At não pode ser no passado";
    public static final String AUTHORIZATION_CODE_EXPIRED = "Authorization Code expirado";
    public static final String AUTHORIZATION_CODE_ALREADY_USED = "Authorization Code já foi utilizado";
    public static final String USED_NULL = "Used não pode ser nulo";

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
     * 
     * @param builder Builder com os dados do AuthorizationCode
     */
    private AuthorizationCode(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.user = builder.user;
        this.systemId = builder.systemId;
        this.expiresAt = builder.expiresAt;
        this.used = builder.used;
        
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
        if (expiresAt == null) {
            throw new DomainException(EXPIRES_AT_NULL);
        }
        if (expiresAt.isBefore(Instant.now())) {
            throw new DomainException(EXPIRES_AT_PAST);
        }

        return AuthorizationCode.builder()
                .id(id)
                .code(UUID.randomUUID().toString())
                .user(user)
                .systemId(systemId)
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
            throw new DomainException(ID_NULL);
        }
        if (user == null) {
            throw new DomainException(USER_NULL);
        }
        if (systemId == null) {
            throw new DomainException(SYSTEM_ID_NULL);
        }
        if (code == null || code.trim().isEmpty()) {
             throw new DomainException(CODE_NULL_OR_BLANK);
        }
        if (expiresAt == null) { // Valida apenas presença, não se está no passado
            throw new DomainException(EXPIRES_AT_NULL);
        }
        if (used == null) {
            throw new DomainException(USED_NULL);
        }
    }

    /**
     * Valida se o code pode ser utilizado.
     */
    public void validateUsable() {
        if (isExpired()) {
            throw new DomainException(AUTHORIZATION_CODE_EXPIRED);
        }
        if (isUsed()) {
            throw new DomainException(AUTHORIZATION_CODE_ALREADY_USED);
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

    
    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthorizationCodeId id;
        private String code;
        private User user;
        private SystemId systemId;
        private Instant expiresAt;
        private Boolean used;

        public Builder id(AuthorizationCodeId id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder systemId(SystemId systemId) {
            this.systemId = systemId;
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

        public AuthorizationCode build() {
            if (id == null) {
                throw new DomainException(ID_NULL);
            }
            if (code == null || code.trim().isEmpty()) {
                throw new DomainException(CODE_NULL_OR_BLANK);
            }
            if (user == null) {
                throw new DomainException(USER_NULL);
            }
            if (systemId == null) {
                throw new DomainException(SYSTEM_ID_NULL);
            }
            if (expiresAt == null) {
                throw new DomainException(EXPIRES_AT_NULL);
            }
            if (used == null) {
                throw new DomainException(USED_NULL);
            }
            return new AuthorizationCode(this);
        }
    }
}
