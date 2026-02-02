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
        AuthorizationCode authCode = AuthorizationCode.builder()
                .id(id)
                .code(code)
                .user(user)
                .systemId(systemId)
                .expiresAt(futureExpiration)
                .used(true)
                .build();

        assertNotNull(authCode);
        assertEquals(code, authCode.getCode());
        assertTrue(authCode.getUsed());
    }

    @Test
    void createWithInvalidExpiration() {
        Instant pastExpiration = Instant.now().minus(1, ChronoUnit.MINUTES);
        
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, systemId, pastExpiration));
        
        assertEquals(AuthorizationCode.EXPIRES_AT_PAST, ex.getMessage());
    }
    
    @Test
    void createWithNullExpiration() {
        DomainException ex = assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, systemId, null));
        
        assertEquals(AuthorizationCode.EXPIRES_AT_NULL, ex.getMessage());
    }

    // ==================== Validação de Invariantes (Contrutor) ====================

    @Test
    void createWithNullId() {
        assertThrows(DomainException.class,
                () -> AuthorizationCode.create(null, user, systemId, futureExpiration));
    }

    @Test
    void createWithNullUser() {
        assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, null, systemId, futureExpiration));
    }

    @Test
    void createWithNullSystem() {
        assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, null, futureExpiration));
    }

    @Test
    void createWithNullExpiresAt() {
        assertThrows(DomainException.class,
                () -> AuthorizationCode.create(id, user, systemId, null));
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
        // Usando o builder diretamente para "burlar" a validação de criação (create) 
        // e simular um objeto recuperado do banco que já expirou
        AuthorizationCode authCode = AuthorizationCode.builder()
                .id(id)
                .code(UUID.randomUUID().toString())
                .user(user)
                .systemId(systemId)
                .expiresAt(pastExpiration)
                .used(false)
                .build();
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals(AuthorizationCode.AUTHORIZATION_CODE_EXPIRED, ex.getMessage());
        assertTrue(authCode.isExpired());
    }

    @Test
    void builderWithoutCode() {
        DomainException ex = assertThrows(DomainException.class, () -> AuthorizationCode.builder()
                .id(id)
                //.code("code") -> Missing
                .user(user)
                .systemId(systemId)
                .expiresAt(futureExpiration)
                .used(false)
                .build());
        assertEquals(AuthorizationCode.CODE_NULL_OR_BLANK, ex.getMessage());
    }

    @Test
    void builderWithoutUsed() {
        DomainException ex = assertThrows(DomainException.class, () -> AuthorizationCode.builder()
                .id(id)
                .code("code")
                .user(user)
                .systemId(systemId)
                .expiresAt(futureExpiration)
                //.used(false) -> Missing
                .build());
        assertEquals(AuthorizationCode.USED_NULL, ex.getMessage());
    }

    @Test
    void validateUsableAlreadyUsed() {
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        authCode.markAsUsed();
        
        DomainException ex = assertThrows(DomainException.class, authCode::validateUsable);
        assertEquals(AuthorizationCode.AUTHORIZATION_CODE_ALREADY_USED, ex.getMessage());
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
        AuthorizationCode authCode = AuthorizationCode.create(id, user, systemId, futureExpiration);
        authCode.markAsUsed();
        
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
