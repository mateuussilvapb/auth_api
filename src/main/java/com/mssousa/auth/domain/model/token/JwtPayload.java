package com.mssousa.auth.domain.model.token;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Value Object que representa o payload lógico de um JWT.
 * <p>
 * Este objeto é um snapshot imutável das decisões de autenticação
 * e autorização tomadas pelo Auth Server.
 * </p>
 * <p>
 * Imutável e thread-safe.
 * </p>
 */
public final class JwtPayload {

    // ==================== Mensagens de Erro ====================

    // RFC Claims
    public static final String ERROR_ISSUER_REQUIRED = "Issuer (iss) é obrigatório";
    public static final String ERROR_SUBJECT_REQUIRED = "Subject (sub) é obrigatório";
    public static final String ERROR_AUDIENCE_REQUIRED = "Audience (aud) é obrigatório";
    public static final String ERROR_ISSUED_AT_REQUIRED = "IssuedAt (iat) é obrigatório";
    public static final String ERROR_EXPIRES_AT_REQUIRED = "ExpiresAt (exp) é obrigatório";
    public static final String ERROR_JWT_ID_REQUIRED = "JWT ID (jti) é obrigatório";
    public static final String ERROR_INVALID_EXPIRATION = "Data de expiração do token é inválida";

    // User Identity
    public static final String ERROR_USER_ID_REQUIRED = "UserId é obrigatório";
    public static final String ERROR_USERNAME_REQUIRED = "Username é obrigatório";
    public static final String ERROR_EMAIL_REQUIRED = "Email é obrigatório";
    public static final String ERROR_NAME_REQUIRED = "Name é obrigatório";

    // System Context
    public static final String ERROR_SYSTEM_ID_REQUIRED = "SystemId é obrigatório";
    public static final String ERROR_SYSTEM_ROLES_REQUIRED = "SystemRoles é obrigatório para usuários não-master";

    // Security
    public static final String ERROR_AUTH_METHOD_REQUIRED = "Auth method é obrigatório";
    public static final String ERROR_SESSION_ID_REQUIRED = "Session ID é obrigatório";

    // ==================== Claims RFC ====================

    private final String issuer;      // iss
    private final String subject;     // sub
    private final String audience;    // aud
    private final Instant issuedAt;   // iat
    private final Instant expiresAt;  // exp
    private final String jwtId;       // jti

    // ==================== Identidade do Usuário ====================

    private final UserId userId;
    private final String username;
    private final String email;
    private final String name;

    // ==================== Papel Global ====================

    private final boolean master;

    // ==================== Contexto do Sistema ====================

    private final SystemId systemId;
    private final List<String> systemRoles;

    // ==================== Segurança ====================

    private final String authMethod;
    private final String sessionId;
    private final int tokenVersion;

    // ==================== Construtor Privado ====================

    private JwtPayload(Builder builder) {
        this.issuer = require(builder.issuer, ERROR_ISSUER_REQUIRED);
        this.subject = require(builder.subject, ERROR_SUBJECT_REQUIRED);
        this.audience = require(builder.audience, ERROR_AUDIENCE_REQUIRED);
        this.issuedAt = require(builder.issuedAt, ERROR_ISSUED_AT_REQUIRED);
        this.expiresAt = require(builder.expiresAt, ERROR_EXPIRES_AT_REQUIRED);
        this.jwtId = require(builder.jwtId, ERROR_JWT_ID_REQUIRED);

        validateExpiration(builder.issuedAt, builder.expiresAt);

        this.userId = require(builder.userId, ERROR_USER_ID_REQUIRED);
        this.username = require(builder.username, ERROR_USERNAME_REQUIRED);
        this.email = require(builder.email, ERROR_EMAIL_REQUIRED);
        this.name = require(builder.name, ERROR_NAME_REQUIRED);

        this.master = builder.master;

        this.systemId = require(builder.systemId, ERROR_SYSTEM_ID_REQUIRED);
        this.systemRoles = buildSystemRoles(builder.master, builder.systemRoles);

        this.authMethod = require(builder.authMethod, ERROR_AUTH_METHOD_REQUIRED);
        this.sessionId = require(builder.sessionId, ERROR_SESSION_ID_REQUIRED);
        this.tokenVersion = builder.tokenVersion;
    }

    // ==================== Validações ====================

    private void validateExpiration(Instant issuedAt, Instant expiresAt) {
        if (expiresAt.isBefore(issuedAt)) {
            throw new DomainException(ERROR_INVALID_EXPIRATION);
        }
    }

    private List<String> buildSystemRoles(boolean master, List<String> systemRoles) {
        if (master) {
            // Master não precisa de roles (acesso total)
            return systemRoles == null ? List.of() : List.copyOf(systemRoles);
        }
        
        // Não-master DEVE ter roles
        return List.copyOf(require(systemRoles, ERROR_SYSTEM_ROLES_REQUIRED));
    }

    private static <T> T require(T value, String message) {
        if (value == null || (value instanceof List && ((List<?>) value).isEmpty())) {
            throw new DomainException(message);
        }
        return value;
    }

    // ==================== Getters ====================

    public String issuer() {
        return issuer;
    }

    public String subject() {
        return subject;
    }

    public String audience() {
        return audience;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    public Instant expiresAt() {
        return expiresAt;
    }

    public String jwtId() {
        return jwtId;
    }

    public UserId userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String name() {
        return name;
    }

    public boolean isMaster() {
        return master;
    }

    public SystemId systemId() {
        return systemId;
    }

    public List<String> systemRoles() {
        return systemRoles;
    }

    public String authMethod() {
        return authMethod;
    }

    public String sessionId() {
        return sessionId;
    }

    public int tokenVersion() {
        return tokenVersion;
    }

    // ==================== Comportamentos ====================

    /**
     * Verifica se o token está expirado.
     *
     * @param now momento atual
     * @return true se o token está expirado
     */
    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    /**
     * Verifica se o token ainda é válido (não expirado).
     *
     * @param now momento atual
     * @return true se o token ainda é válido
     */
    public boolean isValid(Instant now) {
        return !isExpired(now);
    }

    // ==================== Equality ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtPayload other)) return false;
        return master == other.master
                && tokenVersion == other.tokenVersion
                && issuer.equals(other.issuer)
                && subject.equals(other.subject)
                && audience.equals(other.audience)
                && issuedAt.equals(other.issuedAt)
                && expiresAt.equals(other.expiresAt)
                && jwtId.equals(other.jwtId)
                && userId.equals(other.userId)
                && username.equals(other.username)
                && email.equals(other.email)
                && name.equals(other.name)
                && systemId.equals(other.systemId)
                && systemRoles.equals(other.systemRoles)
                && authMethod.equals(other.authMethod)
                && sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                issuer, subject, audience, issuedAt, expiresAt, jwtId,
                userId, username, email, name, master,
                systemId, systemRoles,
                authMethod, sessionId, tokenVersion);
    }

    @Override
    public String toString() {
        return "JwtPayload{" +
                "sub='" + subject + '\'' +
                ", username='" + username + '\'' +
                ", master=" + master +
                ", systemId=" + systemId +
                ", roles=" + systemRoles.size() +
                ", exp=" + expiresAt +
                '}';
    }

    // ==================== Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        // Claims RFC
        private String issuer;
        private String subject;
        private String audience;
        private Instant issuedAt;
        private Instant expiresAt;
        private String jwtId;

        // Identidade
        private UserId userId;
        private String username;
        private String email;
        private String name;

        // Papel
        private boolean master = false;

        // Sistema
        private SystemId systemId;
        private List<String> systemRoles = new ArrayList<>();

        // Segurança
        private String authMethod = "password";  // Default
        private String sessionId;
        private int tokenVersion = 1;  // Default

        // ==================== RFC Claims ====================

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder audience(String audience) {
            this.audience = audience;
            return this;
        }

        public Builder issuedAt(Instant issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder jwtId(String jwtId) {
            this.jwtId = jwtId;
            return this;
        }

        // ==================== Identidade ====================

        public Builder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        // ==================== Papel ====================

        public Builder master(boolean master) {
            this.master = master;
            return this;
        }

        // ==================== Sistema ====================

        public Builder systemId(SystemId systemId) {
            this.systemId = systemId;
            return this;
        }

        public Builder systemRoles(List<String> systemRoles) {
            this.systemRoles = systemRoles != null 
                ? new ArrayList<>(systemRoles) 
                : new ArrayList<>();
            return this;
        }

        public Builder addSystemRole(String role) {
            if (role != null && !role.isBlank()) {
                this.systemRoles.add(role);
            }
            return this;
        }

        // ==================== Segurança ====================

        public Builder authMethod(String authMethod) {
            this.authMethod = authMethod;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder tokenVersion(int tokenVersion) {
            this.tokenVersion = tokenVersion;
            return this;
        }

        // ==================== Build ====================

        public JwtPayload build() {
            return new JwtPayload(this);
        }
    }
}