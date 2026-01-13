package com.mssousa.auth.domain.model.token.passwordResetToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ResetTokenValueTest {

    @Test
    @DisplayName("Deve criar ResetTokenValue válido")
    void shouldCreateValidResetTokenValue() {
        String validToken = "12345678901234567890123456789012";
        ResetTokenValue value = new ResetTokenValue(validToken);

        assertEquals(validToken, value.value());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor for nulo")
    void shouldThrowException_WhenValueIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ResetTokenValue(null));
        assertEquals("Password reset token não pode ser nulo ou vazio", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Deve lançar exceção quando valor for vazio ou em branco")
    void shouldThrowException_WhenValueIsBlank(String invalidValue) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ResetTokenValue(invalidValue));
        assertEquals("Password reset token não pode ser nulo ou vazio", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor for menor que 32 caracteres")
    void shouldThrowException_WhenValueRunIsTooShort() {
        String shortToken = "1234567890123456789012345678901"; // 31 chars
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new ResetTokenValue(shortToken));
        assertEquals("Password reset token inválido", ex.getMessage());
    }

    @Test
    @DisplayName("Deve verificar igualdade corretamente")
    void shouldCheckEquality() {
        String tokenStr = "12345678901234567890123456789012";
        ResetTokenValue val1 = new ResetTokenValue(tokenStr);
        ResetTokenValue val2 = new ResetTokenValue(tokenStr);
        ResetTokenValue val3 = new ResetTokenValue("98765432109876543210987654321098");

        assertEquals(val1, val2);
        assertNotEquals(val1, val3);
        assertEquals(val1.hashCode(), val2.hashCode());
    }
}
