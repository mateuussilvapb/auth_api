package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void testCreatePasswordFromPlainText() {
        Password password = Password.fromPlainText("mySecurePass123");
        
        assertNotNull(password);
        assertNotNull(password.hashedValue());
        assertTrue(password.hashedValue().startsWith("$2a$")); // BCrypt hash prefix
    }

    @Test
    void testPasswordMatches() {
        String plainPassword = "mySecurePassword";
        Password password = Password.fromPlainText(plainPassword);
        
        assertTrue(password.matches(plainPassword));
    }

    @Test
    void testPasswordDoesNotMatch() {
        Password password = Password.fromPlainText("correctPassword");
        
        assertFalse(password.matches("wrongPassword"));
    }

    @Test
    void testPasswordMatchesWithNull() {
        Password password = Password.fromPlainText("myPassword");
        
        assertFalse(password.matches(null));
    }

    @Test
    void testInvalidPasswordNull() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromPlainText(null));
        assertEquals("Senha não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testInvalidPasswordEmpty() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromPlainText(""));
        assertEquals("Senha não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testInvalidPasswordBlank() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromPlainText("   "));
        assertEquals("Senha não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testInvalidPasswordTooShort() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromPlainText("short"));
        assertEquals("Senha deve ter pelo menos 8 caracteres", exception.getMessage());
    }

    @Test
    void testPasswordFromHash() {
        String plainPassword = "testPassword123";
        Password originalPassword = Password.fromPlainText(plainPassword);
        String hash = originalPassword.hashedValue();
        
        Password reconstructedPassword = Password.fromHash(hash);
        
        assertNotNull(reconstructedPassword);
        assertEquals(hash, reconstructedPassword.hashedValue());
        assertTrue(reconstructedPassword.matches(plainPassword));
    }

    @Test
    void testPasswordFromHashInvalidNull() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromHash(null));
        assertEquals("Senha hash não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testPasswordFromHashInvalidEmpty() {
        DomainException exception = assertThrows(DomainException.class, 
            () -> Password.fromHash(""));
        assertEquals("Senha hash não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testPasswordEquality() {
        String plainPassword = "testPassword";
        Password password1 = Password.fromPlainText(plainPassword);
        String hash = password1.hashedValue();
        Password password2 = Password.fromHash(hash);
        
        assertEquals(password1, password2);
    }

    @Test
    void testPasswordEqualityDifferentHashes() {
        Password password1 = Password.fromPlainText("password123");
        Password password2 = Password.fromPlainText("password123");
        
        // Os hashes serão diferentes devido ao salt do BCrypt
        assertNotEquals(password1.hashedValue(), password2.hashedValue());
        assertNotEquals(password1, password2);
    }

    @Test
    void testPasswordHashCode() {
        String plainPassword = "testPassword";
        Password password1 = Password.fromPlainText(plainPassword);
        String hash = password1.hashedValue();
        Password password2 = Password.fromHash(hash);
        
        assertEquals(password1.hashCode(), password2.hashCode());
    }

    @Test
    void testPasswordMinimumLength() {
        Password password = Password.fromPlainText("12345678"); // Exactly 8 characters
        
        assertNotNull(password);
        assertTrue(password.matches("12345678"));
    }
}
