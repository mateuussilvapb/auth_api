package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testValidEmail() {
        Email email = new Email("user@example.com");
        assertEquals("user@example.com", email.value());
    }

    @Test
    void testEmailNormalization() {
        Email email = new Email("ADMIN@EXAMPLE.COM");
        assertEquals("admin@example.com", email.value());
    }

    @Test
    void testEmailNormalizationWithMixedCase() {
        Email email = new Email("User.Name@Example.COM");
        assertEquals("user.name@example.com", email.value());
    }

    @Test
    void testEmailTrimming() {
        Email email = new Email("  user@example.com  ");
        assertEquals("user@example.com", email.value());
    }

    @Test
    void testInvalidEmailNull() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email(null));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailEmpty() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email(""));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailBlank() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email("   "));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailFormat() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email("invalid-email"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingAt() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email("userexample.com"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingDomain() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email("user@"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingTLD() {
        DomainException exception = assertThrows(DomainException.class, () -> new Email("user@example"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testEmailEquality() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("TEST@EXAMPLE.COM"); // Should be normalized
        Email email3 = new Email("other@example.com");

        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
    }

    @Test
    void testEmailHashCode() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("TEST@EXAMPLE.COM");

        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void testEmailToString() {
        Email email = new Email("TEST@EXAMPLE.COM");
        assertEquals("test@example.com", email.toString());
    }
}
