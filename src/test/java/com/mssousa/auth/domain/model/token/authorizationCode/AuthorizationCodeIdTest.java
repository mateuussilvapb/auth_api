package com.mssousa.auth.domain.model.token.authorizationCode;

import com.mssousa.auth.domain.exception.DomainException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationCodeIdTest {

    @Test
    void testValidAuthorizationCodeId() {
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(1L);
        assertEquals(1L, authorizationCodeId.value());
    }

    @Test
    void testValidAuthorizationCodeIdLargeNumber() {
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(999999999L);
        assertEquals(999999999L, authorizationCodeId.value());
    }

    @Test
    void testInvalidAuthorizationCodeIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> AuthorizationCodeId.of(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidAuthorizationCodeIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> AuthorizationCodeId.of(0L));
        assertEquals("AuthorizationCodeId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidAuthorizationCodeIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> AuthorizationCodeId.of(-1L));
        assertEquals("AuthorizationCodeId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidAuthorizationCodeIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> AuthorizationCodeId.of(-999999L));
        assertEquals("AuthorizationCodeId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testAuthorizationCodeIdEquality() {
        AuthorizationCodeId authorizationCodeId1 = AuthorizationCodeId.of(123L);
        AuthorizationCodeId authorizationCodeId2 = AuthorizationCodeId.of(123L);
        AuthorizationCodeId authorizationCodeId3 = AuthorizationCodeId.of(456L);

        assertEquals(authorizationCodeId1, authorizationCodeId2);
        assertNotEquals(authorizationCodeId1, authorizationCodeId3);
    }

    @Test
    void testAuthorizationCodeIdHashCode() {
        AuthorizationCodeId authorizationCodeId1 = AuthorizationCodeId.of(123L);
        AuthorizationCodeId authorizationCodeId2 = AuthorizationCodeId.of(123L);

        assertEquals(authorizationCodeId1.hashCode(), authorizationCodeId2.hashCode());
    }

    @Test
    void testAuthorizationCodeIdToString() {
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(12345L);
        assertEquals("12345", authorizationCodeId.toString());
    }

    @Test
    void testAuthorizationCodeIdValue() {
        Long expectedValue = 42L;
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(expectedValue);

        assertEquals(expectedValue, authorizationCodeId.value());
    }

    @Test
    void testDifferentAuthorizationCodeIdsNotEqual() {
        AuthorizationCodeId authorizationCodeId1 = AuthorizationCodeId.of(1L);
        AuthorizationCodeId authorizationCodeId2 = AuthorizationCodeId.of(2L);

        assertNotEquals(authorizationCodeId1, authorizationCodeId2);
        assertNotEquals(authorizationCodeId1.hashCode(), authorizationCodeId2.hashCode());
    }

    @Test
    void testAuthorizationCodeIdImmutability() {
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(100L);
        Long value = authorizationCodeId.value();

        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, authorizationCodeId.value());
        assertEquals(100L, authorizationCodeId.value());
    }

    @Test
    void testAuthorizationCodeIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        AuthorizationCodeId authorizationCodeId = AuthorizationCodeId.of(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, authorizationCodeId.value());
    }
}
