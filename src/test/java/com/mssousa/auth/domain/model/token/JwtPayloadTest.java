package com.mssousa.auth.domain.model.token;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtPayloadTest {

    private String issuer;
    private String subject;
    private String audience;
    private Instant issuedAt;
    private Instant expiresAt;
    private String jwtId;

    private UserId userId;
    private String username;
    private String email;
    private String name;

    private boolean master;

    private SystemId systemId;
    private List<String> systemRoles;

    private String authMethod;
    private String sessionId;
    private int tokenVersion;

    @BeforeEach
    void setUp() {
        issuer = "https://auth.example.com";
        subject = "user-123";
        audience = "client-system";
        issuedAt = Instant.now();
        expiresAt = issuedAt.plusSeconds(3600);
        jwtId = "jwt-id-123";

        userId = new UserId(1L);
        username = "testuser";
        email = "test@example.com";
        name = "Test User";

        master = false;

        systemId = new SystemId(10L);
        systemRoles = List.of("ADMIN", "TEACHER");

        authMethod = "password";
        sessionId = "session-abc";
        tokenVersion = 1;
    }

    // ==================== Criação e Getters ====================

    @Test
    void shouldCreateValidJwtPayload() {
        JwtPayload payload = createPayload();

        assertNotNull(payload);
        assertEquals(issuer, payload.issuer());
        assertEquals(subject, payload.subject());
        assertEquals(audience, payload.audience());
        assertEquals(issuedAt, payload.issuedAt());
        assertEquals(expiresAt, payload.expiresAt());
        assertEquals(jwtId, payload.jwtId());

        assertEquals(userId, payload.userId());
        assertEquals(username, payload.username());
        assertEquals(email, payload.email());
        assertEquals(name, payload.name());

        assertFalse(payload.isMaster());

        assertEquals(systemId, payload.systemId());
        assertEquals(systemRoles, payload.systemRoles());

        assertEquals(authMethod, payload.authMethod());
        assertEquals(sessionId, payload.sessionId());
        assertEquals(tokenVersion, payload.tokenVersion());
    }

    // ==================== Validações Obrigatórias ====================

    @Test
    void shouldFailWhenIssuerIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        null, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Issuer (iss) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenSubjectIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, null, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Subject (sub) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenExpirationIsBeforeIssuedAt() {
        Instant invalidExp = issuedAt.minusSeconds(10);

        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, invalidExp, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Data de expiração do token é inválida", ex.getMessage());
    }

    @Test
    void shouldFailWhenAudienceIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, null, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Audience (aud) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenIssuedAtIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, null, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("IssuedAt (iat) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenExpiresAtIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, null, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("ExpiresAt (exp) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenJwtIdIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, null,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("JWT ID (jti) é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenUserIdIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        null, username, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("UserId é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, null, email, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Username é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenEmailIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, null, name, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Email é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, null, master,
                        systemId, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("Name é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenSystemIdIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        null, systemRoles, authMethod, sessionId, tokenVersion));

        assertEquals("SystemId é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenAuthMethodIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, null, sessionId, tokenVersion));

        assertEquals("Auth method é obrigatório", ex.getMessage());
    }

    @Test
    void shouldFailWhenSessionIdIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, master,
                        systemId, systemRoles, authMethod, null, tokenVersion));

        assertEquals("Session ID é obrigatório", ex.getMessage());
    }

    @Test
    void shouldCreatePayloadWhenMasterAndSystemRolesIsNull() {
        JwtPayload payload = new JwtPayload(
                issuer, subject, audience, issuedAt, expiresAt, jwtId,
                userId, username, email, name, true, // Master = true
                systemId, null, authMethod, sessionId, tokenVersion); // Roles = null

        assertNotNull(payload);
        assertTrue(payload.isMaster());
        assertNotNull(payload.systemRoles());
        assertTrue(payload.systemRoles().isEmpty());
    }

    @Test
    void shouldFailWhenNotMasterAndSystemRolesIsNull() {
        DomainException ex = assertThrows(DomainException.class,
                () -> new JwtPayload(
                        issuer, subject, audience, issuedAt, expiresAt, jwtId,
                        userId, username, email, name, false, // Master = false
                        systemId, null, authMethod, sessionId, tokenVersion)); // Roles = null

        assertEquals("SystemRoles é obrigatório", ex.getMessage());
    }

    // ==================== Imutabilidade ====================

    @Test
    void systemRolesShouldBeImmutable() {
        JwtPayload payload = createPayload();

        assertThrows(UnsupportedOperationException.class,
                () -> payload.systemRoles().add("HACKER"));
    }

    // ==================== Igualdade ====================

    @Test
    void payloadsWithSameDataShouldBeEqual() {
        JwtPayload p1 = createPayload();
        JwtPayload p2 = createPayload();

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void payloadsWithDifferentJwtIdShouldNotBeEqual() {
        JwtPayload p1 = createPayload();

        JwtPayload p2 = new JwtPayload(
                issuer, subject, audience, issuedAt, expiresAt, "different-jti",
                userId, username, email, name, master,
                systemId, systemRoles, authMethod, sessionId, tokenVersion);

        assertNotEquals(p1, p2);
    }

    // ==================== Helpers ====================

    private JwtPayload createPayload() {
        return new JwtPayload(
                issuer,
                subject,
                audience,
                issuedAt,
                expiresAt,
                jwtId,
                userId,
                username,
                email,
                name,
                master,
                systemId,
                systemRoles,
                authMethod,
                sessionId,
                tokenVersion
        );
    }
}
