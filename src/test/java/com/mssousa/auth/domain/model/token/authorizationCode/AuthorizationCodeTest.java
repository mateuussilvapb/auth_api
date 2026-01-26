package com.mssousa.auth.domain.model.token.authorizationCode;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;

class AuthorizationCodeTest {

    private AuthorizationCodeId id;
    private User user;
    private SystemId systemId;
    private Instant futureExpiration;

    @BeforeEach
    void setUp() {
        id = AuthorizationCodeId.of(1L);
        futureExpiration = Instant.now().plus(10, ChronoUnit.MINUTES);

        // Setup User
        user = User.builder()
                .id(UserId.of(1L))
                .username(Username.of("testuser"))
                .email(Email.of("test@example.com"))
                .password(Password.fromPlainText("password123"))
                .master(false)
                .status(UserStatus.ACTIVE)
                .name("Test User")
                .build();

        // Setup ClientSystem
        systemId = SystemId.of(1L);
    }

    // ==================== Criação e Factory ====================

    @Test
    void createNewAuthorizationCode() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);

        assertNotNull(authCode);
        assertNotNull(authCode.getCode());
        assertFalse(authCode.getCode().isEmpty());
        assertEquals(id, authCode.getId());
        assertEquals(user, authCode.getUser());
        assertEquals(systemId, authCode.getSystemId());
        assertEquals(futureExpiration, authCode.getExpiresAt());
        assertFalse(authCode.getUsed());
        assertFalse(authCode.isUsed());
        assertFalse(authCode.isExpired());
    }

    @Test
    void reconstituteAuthorizationCode() {
        String code = UUID.randomUUID().toString();
        AuthorizationCode authCode = new AuthorizationCode(id, code, user, systemId, futureExpiration, true);

        assertNotNull(authCode);
        assertEquals(code, authCode.getCode());
        assertTrue(authCode.getUsed());
    }

    @Test
    void createWithInvalidExpiration() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.MINUTES);
        
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, systemId, pastExpiration));
        
        assertEquals("Data de expiração inválida para novos AuthorizationCode", ex.getMessage());
    }
    
    @Test
    void createWithNullExpiration() {
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, systemId, null));
        
        assertEquals("Data de expiração inválida para novos AuthorizationCode", ex.getMessage());
    }

    // ==================== Validação de Invariantes (Contrutor) ====================

    @Test
    void createWithNullId() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(null, "code", user, systemId, futureExpiration, false));
    }

    @Test
    void createWithNullUser() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", null, systemId, futureExpiration, false));
    }

    @Test
    void createWithNullSystem() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", user, null, futureExpiration, false));
    }

    @Test
    void createWithNullCode() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, null, user, systemId, futureExpiration, false));
    }

    @Test
    void createWithEmptyCode() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "   ", user, systemId, futureExpiration, false));
    }

    @Test
    void createWithNullExpiresAt() {
        assertThrows(DomainException.class,
                () -> new AuthorizationCode(id, "code", user, systemId, null, false));
    }

    // ==================== Regras de Negócio e Validações ====================

    @Test
    void validateUsableSuccess() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        assertDoesNotThrow(authCode::validateUsable);
    }

    @Test
    void validateUsableExpired() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.MINUTES);
        // Usando o construtor para forçar um objeto expirado (pois o factory não permite)
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, systemId, pastExpiration, false);
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals("AuthorizationCode expirado", ex.getMessage());
        assertTrue(authCode.isExpired());
    }

    @Test
    void validateUsableAlreadyUsed() {
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, systemId, futureExpiration, true);
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals("AuthorizationCode já foi utilizado", ex.getMessage());
        assertTrue(authCode.isUsed());
    }

    @Test
    void markAsUsedSuccess() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        
        assertFalse(authCode.isUsed());
        authCode.markAsUsed();
        assertTrue(authCode.isUsed());
    }

    @Test
    void markAsUsedExampleFail() {
        // Se tentar marcar como usado algo que já foi usado ou expirou, deve falhar no validateUsable interno
        AuthorizationCode authCode = new AuthorizationCode(id, "code", user, systemId, futureExpiration, true);
        
        assertThrows(DomainException.class, authCode::markAsUsed);
    }

    @Test
    void belongsToSameSystem() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        assertTrue(authCode.belongsTo(systemId));
    }

    @Test
    void belongsToDifferentSystem() {
        SystemId otherSystemId = SystemId.of(2L);

        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        assertFalse(authCode.belongsTo(otherSystemId));
    }
}
