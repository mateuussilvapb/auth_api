package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testValidEmail() {
        Email email = Email.of("user@example.com");
        assertEquals("user@example.com", email.value());
    }

    @Test
    void testEmailNormalization() {
        Email email = Email.of("ADMIN@EXAMPLE.COM");
        assertEquals("admin@example.com", email.value());
    }

    @Test
    void testEmailNormalizationWithMixedCase() {
        Email email = Email.of("User.Name@Example.COM");
        assertEquals("user.name@example.com", email.value());
    }

    @Test
    void testEmailTrimming() {
        Email email = Email.of("  user@example.com  ");
        assertEquals("user@example.com", email.value());
    }

    @Test
    void testInvalidEmailNull() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of(null));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailEmpty() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of(""));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailBlank() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of("   "));
        assertEquals("Email não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testInvalidEmailFormat() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of("invalid-email"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingAt() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of("userexample.com"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingDomain() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of("user@"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testInvalidEmailMissingTLD() {
        DomainException exception = assertThrows(DomainException.class, () -> Email.of("user@example"));
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    void testEmailEquality() {
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("TEST@EXAMPLE.COM"); // Should be normalized
        Email email3 = Email.of("other@example.com");

        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
    }

    @Test
    void testEmailHashCode() {
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("TEST@EXAMPLE.COM");

        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void testEmailToString() {
        Email email = Email.of("TEST@EXAMPLE.COM");
        assertEquals("test@example.com", email.toString());
    }
}
