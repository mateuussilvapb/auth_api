package com.mssousa.auth.domain.model.token.passwordResetToken;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.user.UserId;

class PasswordResetTokenTest {

    private PasswordResetTokenId id;
    private UserId userId;
    private ResetTokenValue value;
    private Instant futureExpiration;

    @BeforeEach
    void setUp() {
        id = PasswordResetTokenId.of(1L);
        userId = UserId.of(123L);
        value = new ResetTokenValue("12345678901234567890123456789012");
        futureExpiration = Instant.now().plus(1, ChronoUnit.HOURS);
    }

    // ==================== Criação e Factory ====================

    @Test
    @DisplayName("create deve criar um token válido com ID, UserID e Expiração")
    void create_ShouldCreateValidToken() {
        PasswordResetToken token = PasswordResetToken.create(id, userId, futureExpiration);

        assertNotNull(token);
        assertEquals(id, token.getId());
        assertEquals(userId, token.getUserId());
        assertEquals(futureExpiration, token.getExpiresAt());
        assertNotNull(token.getValue());
        assertFalse(token.getUsed());
        assertFalse(token.isExpired());
    }

    @Test
    @DisplayName("Reconstituição deve restaurar objeto corretamente")
    void reconstituteToken() {
        PasswordResetToken token = new PasswordResetToken(id, value, userId, futureExpiration, true);

        assertNotNull(token);
        assertEquals(id, token.getId());
        assertEquals(value, token.getValue());
        assertTrue(token.isUsed());
    }

    @Test
    @DisplayName("create deve lançar exceção se data de expiração for nula")
    void create_ShouldThrowException_WhenExpiresAtIsNull() {
        assertThrows(DomainException.class, () -> PasswordResetToken.create(id, userId, null));
    }

    @Test
    @DisplayName("create deve lançar exceção se data de expiração for no passado")
    void create_ShouldThrowException_WhenExpiresAtIsInPast() {
        Instant past = Instant.now().minus(1, ChronoUnit.HOURS);
        assertThrows(DomainException.class, () -> PasswordResetToken.create(id, userId, past));
    }

    // ==================== Validação de Invariantes (Contrutor) ====================

    @Test
    @DisplayName("Construtor deve falhar se ID for nulo")
    void constructor_ShouldThrowException_WhenIdIsNull() {
        assertThrows(DomainException.class, () -> 
            new PasswordResetToken(null, value, userId, futureExpiration, false));
    }

    @Test
    @DisplayName("Construtor deve falhar se Value for nulo")
    void constructor_ShouldThrowException_WhenValueIsNull() {
        assertThrows(DomainException.class, () -> 
            new PasswordResetToken(id, null, userId, futureExpiration, false));
    }

    @Test
    @DisplayName("Construtor deve falhar se UserId for nulo")
    void constructor_ShouldThrowException_WhenUserIdIsNull() {
        assertThrows(DomainException.class, () -> 
            new PasswordResetToken(id, value, null, futureExpiration, false));
    }

    @Test
    @DisplayName("Construtor deve falhar se ExpiresAt for nulo")
    void constructor_ShouldThrowException_WhenExpiresAtIsNull() {
        assertThrows(DomainException.class, () -> 
            new PasswordResetToken(id, value, userId, null, false));
    }

    @Test
    @DisplayName("Construtor deve falhar se Used for nulo")
    void constructor_ShouldThrowException_WhenUsedIsNull() {
        assertThrows(DomainException.class, () -> 
            new PasswordResetToken(id, value, userId, futureExpiration, null));
    }

    // ==================== Regras de Negócio e Validações ====================

    @Test
    @DisplayName("validateUsable deve validar com sucesso se token for válido")
    void validateUsable_ShouldSucceed_WhenTokenIsValid() {
        PasswordResetToken token = PasswordResetToken.create(id, userId, futureExpiration);
        assertDoesNotThrow(token::validateUsable);
    }

    @Test
    @DisplayName("validateUsable deve lançar exceção se token estiver expirado")
    void validateUsable_ShouldThrowException_WhenExpired() {
        Instant past = Instant.now().minus(1, ChronoUnit.MINUTES);
        // Bypass factory to create expired token
        PasswordResetToken token = new PasswordResetToken(id, value, userId, past, false);

        assertTrue(token.isExpired());
        assertThrows(DomainException.class, token::validateUsable);
    }

    @Test
    @DisplayName("markAsUsed deve marcar o token como usado")
    void markAsUsed_ShouldMarkTokenAsUsed() {
        PasswordResetToken token = PasswordResetToken.create(id, userId, futureExpiration);

        token.markAsUsed();

        assertTrue(token.isUsed());
    }

    @Test
    @DisplayName("markAsUsed deve lançar exceção se token já foi usado")
    void markAsUsed_ShouldThrowException_WhenAlreadyUsed() {
        PasswordResetToken token = PasswordResetToken.create(id, userId, futureExpiration);

        token.markAsUsed(); // First time

        assertThrows(DomainException.class, token::markAsUsed); // Second time
    }
}
