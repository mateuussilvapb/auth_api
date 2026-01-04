package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void testValidUsername() {
        Username username = new Username("user123");
        assertEquals("user123", username.value());
    }

    @Test
    void testValidUsernameWithUnderscore() {
        Username username = new Username("john_doe");
        assertEquals("john_doe", username.value());
    }

    @Test
    void testInvalidUsernameNull() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username(null));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameEmpty() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username(""));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameBlank() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username("   "));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameTooShort() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username("ab"));
        assertEquals("Username deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    void testInvalidUsernameTooLong() {
        String longUsername = "a".repeat(51);
        DomainException exception = assertThrows(DomainException.class, () -> new Username(longUsername));
        assertEquals("Username não pode exceder 50 caracteres", exception.getMessage());
    }

    @Test
    void testInvalidUsernameSpecialChars() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username("user@123"));
        assertEquals("Username deve conter apenas caracteres alfanuméricos e sublinhados", exception.getMessage());
    }

    @Test
    void testInvalidUsernameWithSpace() {
        DomainException exception = assertThrows(DomainException.class, () -> new Username("user name"));
        assertEquals("Username deve conter apenas caracteres alfanuméricos e sublinhados", exception.getMessage());
    }

    @Test
    void testUsernameEquality() {
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");
        Username username3 = new Username("otheruser");

        assertEquals(username1, username2);
        assertNotEquals(username1, username3);
    }

    @Test
    void testUsernameHashCode() {
        Username username1 = new Username("testuser");
        Username username2 = new Username("testuser");

        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void testUsernameToString() {
        Username username = new Username("testuser");
        assertEquals("testuser", username.toString());
    }
}
