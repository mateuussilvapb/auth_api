package com.mssousa.auth.domain.model.token;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Value Object que representa o payload lógico de um JWT.
 * <p>
 * Este objeto é um snapshot imutável das decisões de autenticação
 * e autorização tomadas pelo Auth Server.
 * </p>
 */
public final class JwtPayload {

    // ==================== Claims RFC ====================

    private final String issuer; // iss
    private final String subject; // sub
    private final String audience; // aud
    private final Instant issuedAt; // iat
    private final Instant expiresAt; // exp
    private final String jwtId; // jti

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

    // ==================== Construtor ====================

    public JwtPayload(
            String issuer,
            String subject,
            String audience,
            Instant issuedAt,
            Instant expiresAt,
            String jwtId,
            UserId userId,
            String username,
            String email,
            String name,
            boolean master,
            SystemId systemId,
            List<String> systemRoles,
            String authMethod,
            String sessionId,
            int tokenVersion) {
        this.issuer = require(issuer, "Issuer (iss) é obrigatório");
        this.subject = require(subject, "Subject (sub) é obrigatório");
        this.audience = require(audience, "Audience (aud) é obrigatório");
        this.issuedAt = require(issuedAt, "IssuedAt (iat) é obrigatório");
        this.expiresAt = require(expiresAt, "ExpiresAt (exp) é obrigatório");
        this.jwtId = require(jwtId, "JWT ID (jti) é obrigatório");

        if (expiresAt.isBefore(issuedAt)) {
            throw new DomainException("Data de expiração do token é inválida");
        }

        this.userId = require(userId, "UserId é obrigatório");
        this.username = require(username, "Username é obrigatório");
        this.email = require(email, "Email é obrigatório");
        this.name = require(name, "Name é obrigatório");

        this.master = master;

        this.systemId = require(systemId, "SystemId é obrigatório");
        
        if (master) {
            this.systemRoles = systemRoles == null ? List.of() : List.copyOf(systemRoles);
        } else {
            this.systemRoles = List.copyOf(require(systemRoles, "SystemRoles é obrigatório"));
        }

        this.authMethod = require(authMethod, "Auth method é obrigatório");
        this.sessionId = require(sessionId, "Session ID é obrigatório");
        this.tokenVersion = tokenVersion;
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

    // ==================== Helpers ====================

    private static <T> T require(T value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
        return value;
    }

    // ==================== Equality ====================

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JwtPayload other))
            return false;
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
}
