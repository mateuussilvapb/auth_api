package com.mssousa.auth.domain.model.token.passwordResetToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;

class PasswordResetTokenIdTest {
    
     @Test
    void testValidPasswordResetTokenId() {
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(1L);
        assertEquals(1L, passwordResetTokenId.value());
    }

    @Test
    void testValidPasswordResetTokenIdLargeNumber() {
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(999999999L);
        assertEquals(999999999L, passwordResetTokenId.value());
    }

    @Test
    void testInvalidPasswordResetTokenIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new PasswordResetTokenId(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidPasswordResetTokenIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> new PasswordResetTokenId(0L));
        assertEquals("PasswordResetTokenId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidPasswordResetTokenIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new PasswordResetTokenId(-1L));
        assertEquals("PasswordResetTokenId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidPasswordResetTokenIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new PasswordResetTokenId(-999999L));
        assertEquals("PasswordResetTokenId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testPasswordResetTokenIdEquality() {
        PasswordResetTokenId passwordResetTokenId1 = new PasswordResetTokenId(123L);
        PasswordResetTokenId passwordResetTokenId2 = new PasswordResetTokenId(123L);
        PasswordResetTokenId passwordResetTokenId3 = new PasswordResetTokenId(456L);

        assertEquals(passwordResetTokenId1, passwordResetTokenId2);
        assertNotEquals(passwordResetTokenId1, passwordResetTokenId3);
    }

    @Test
    void testPasswordResetTokenIdHashCode() {
        PasswordResetTokenId passwordResetTokenId1 = new PasswordResetTokenId(123L);
        PasswordResetTokenId passwordResetTokenId2 = new PasswordResetTokenId(123L);

        assertEquals(passwordResetTokenId1.hashCode(), passwordResetTokenId2.hashCode());
    }

    @Test
    void testPasswordResetTokenIdToString() {
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(12345L);
        assertEquals("12345", passwordResetTokenId.toString());
    }

    @Test
    void testPasswordResetTokenIdValue() {
        Long expectedValue = 42L;
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(expectedValue);
        
        assertEquals(expectedValue, passwordResetTokenId.value());
    }

    @Test
    void testDifferentPasswordResetTokenIdsNotEqual() {
        PasswordResetTokenId passwordResetTokenId1 = new PasswordResetTokenId(1L);
        PasswordResetTokenId passwordResetTokenId2 = new PasswordResetTokenId(2L);

        assertNotEquals(passwordResetTokenId1, passwordResetTokenId2);
        assertNotEquals(passwordResetTokenId1.hashCode(), passwordResetTokenId2.hashCode());
    }

    @Test
    void testPasswordResetTokenIdImmutability() {
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(100L);
        Long value = passwordResetTokenId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, passwordResetTokenId.value());
        assertEquals(100L, passwordResetTokenId.value());
    }

    @Test
    void testPasswordResetTokenIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        PasswordResetTokenId passwordResetTokenId = new PasswordResetTokenId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, passwordResetTokenId.value());
    }
}
