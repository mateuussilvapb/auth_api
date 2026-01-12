package com.mssousa.auth.domain.model.token.authorizationCode;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.system.SystemStatus;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCode;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCodeId;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;

class AuthorizationCodeTest {

    private AuthorizationCodeId id;
    private User user;
    private ClientSystem system;
    private Instant futureExpiration;

    @BeforeEach
    void setUp() {
        id = new AuthorizationCodeId(1L);
        futureExpiration = Instant.now().plus(10, ChronoUnit.MINUTES);

        // Setup User
        user = new User(
                new UserId(1L),
                new Username("testuser"),
                new Email("test@example.com"),
                Password.fromPlainText("password123"),
                false,
                UserStatus.ACTIVE,
                "Test User");

        // Setup ClientSystem
        system = new ClientSystem(
                new SystemId(1L),
                "Test System",
                "test@example.com",
                "password123",
                "http://localhost:8080",
                SystemStatus.ACTIVE);
    }

    // ==================== Criação e Factory ====================

    @Test
    void createNewAuthorizationCode() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, system, futureExpiration);

        assertNotNull(authCode);
        assertNotNull(authCode.getCode());
        assertFalse(authCode.getCode().isEmpty());
        assertEquals(id, authCode.getId());
        assertEquals(user, authCode.getUser());
        assertEquals(system, authCode.getSystem());
        assertEquals(futureExpiration, authCode.getExpiresAt());
        assertFalse(authCode.getUsed());
        assertFalse(authCode.isUsed());
        assertFalse(authCode.isExpired());
    }

    @Test
    void reconstituteAuthorizationCode() {
        String code = UUID.randomUUID().toString();
        AuthorizationCode authCode = new AuthorizationCode(id, code, user, system, futureExpiration, true);

        assertNotNull(authCode);
        assertEquals(code, authCode.getCode());
        assertTrue(authCode.getUsed());
    }

    @Test
    void createWithInvalidExpiration() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.MINUTES);
        
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, system, pastExpiration));
        
        assertEquals("Data de expiração inválida para novos AuthorizationCode", ex.getMessage());
    }
    
    @Test
    void createWithNullExpiration() {
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, system, null));
        
        assertEquals("Data de expiração inválida para novos AuthorizationCode", ex.getMessage());
    }

    // ==================== Validação de Invariantes (Contrutor) ====================

    @Test
    void createWithNullId() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(null, "code", user, system, futureExpiration, false));
    }

    @Test
    void createWithNullUser() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", null, system, futureExpiration, false));
    }

    @Test
    void createWithNullSystem() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", user, null, futureExpiration, false));
    }

    @Test
    void createWithNullCode() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, null, user, system, futureExpiration, false));
    }

    @Test
    void createWithEmptyCode() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "   ", user, system, futureExpiration, false));
    }

    @Test
    void createWithNullExpiresAt() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", user, system, null, false));
    }

    // ==================== Regras de Negócio e Validações ====================

    @Test
    void validateUsableSuccess() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, system, futureExpiration);
        assertDoesNotThrow(authCode::validateUsable);
    }

    @Test
    void validateUsableExpired() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.MINUTES);
        // Usando o construtor para forçar um objeto expirado (pois o factory não permite)
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, system, pastExpiration, false);
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals("AuthorizationCode expirado", ex.getMessage());
        assertTrue(authCode.isExpired());
    }

    @Test
    void validateUsableAlreadyUsed() {
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, system, futureExpiration, true);
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals("AuthorizationCode já foi utilizado", ex.getMessage());
        assertTrue(authCode.isUsed());
    }

    @Test
    void markAsUsedSuccess() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, system, futureExpiration);
        
        assertFalse(authCode.isUsed());
        authCode.markAsUsed();
        assertTrue(authCode.isUsed());
    }

    @Test
    void markAsUsedExampleFail() {
        // Se tentar marcar como usado algo que já foi usado ou expirou, deve falhar no validateUsable interno
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, system, futureExpiration, true);
        
        assertThrows(DomainException.class, authCode::markAsUsed);
    }

    @Test
    void belongsToSameSystem() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, system, futureExpiration);
        assertTrue(authCode.belongsTo(system));
    }

    @Test
    void belongsToDifferentSystem() {
        ClientSystem otherSystem = new ClientSystem(
                new SystemId(2L),
                "Other System",
                "other@example.com",
                "password123",
                "http://localhost:9090",
                SystemStatus.ACTIVE);

        AuthorizationCode authCode = AuthorizationCode.create(id, user, system, futureExpiration);
        assertFalse(authCode.belongsTo(otherSystem));
    }
}
