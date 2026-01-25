package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void testValidUsername() {
        Username username = Username.of("user123");
        assertEquals("user123", username.value());
    }

    @Test
    void testValidUsernameWithUnderscore() {
        Username username = Username.of("john_doe");
        assertEquals("john_doe", username.value());
    }

    @Test
    void testInvalidUsernameNull() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of(null));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameEmpty() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of(""));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameBlank() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of("   "));
        assertEquals("Username não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidUsernameTooShort() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of("ab"));
        assertEquals("Username deve ter pelo menos 3 caracteres", exception.getMessage());
    }

    @Test
    void testInvalidUsernameTooLong() {
        String longUsername = "a".repeat(51);
        DomainException exception = assertThrows(DomainException.class, () -> Username.of(longUsername));
        assertEquals("Username não pode exceder 50 caracteres", exception.getMessage());
    }

    @Test
    void testInvalidUsernameSpecialChars() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of("user@123"));
        assertEquals("Username deve conter apenas caracteres alfanuméricos e sublinhados", exception.getMessage());
    }

    @Test
    void testInvalidUsernameWithSpace() {
        DomainException exception = assertThrows(DomainException.class, () -> Username.of("user name"));
        assertEquals("Username deve conter apenas caracteres alfanuméricos e sublinhados", exception.getMessage());
    }

    @Test
    void testUsernameEquality() {
        Username username1 = Username.of("testuser");
        Username username2 = Username.of("testuser");
        Username username3 = Username.of("otheruser");

        assertEquals(username1, username2);
        assertNotEquals(username1, username3);
    }

    @Test
    void testUsernameHashCode() {
        Username username1 = Username.of("testuser");
        Username username2 = Username.of("testuser");

        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void testUsernameToString() {
        Username username = Username.of("testuser");
        assertEquals("testuser", username.toString());
    }
}
