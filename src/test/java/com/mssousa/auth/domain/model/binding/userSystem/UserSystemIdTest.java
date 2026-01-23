package com.mssousa.auth.domain.model.binding.userSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;

class UserSystemIdTest {
    
     @Test
    void testValidUserSystemId() {
        UserSystemId userSystemId = UserSystemId.of(1L);
        assertEquals(1L, userSystemId.value());
    }

    @Test
    void testValidUserSystemIdLargeNumber() {
        UserSystemId userSystemId = UserSystemId.of(999999999L);
        assertEquals(999999999L, userSystemId.value());
    }

    @Test
    void testInvalidUserSystemIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> UserSystemId.of(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidUserSystemIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemId.of(0L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemId.of(-1L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemId.of(-999999L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testUserSystemIdEquality() {
        UserSystemId userSystemId1 = UserSystemId.of(123L);
        UserSystemId userSystemId2 = UserSystemId.of(123L);
        UserSystemId userSystemId3 = UserSystemId.of(456L);

        assertEquals(userSystemId1, userSystemId2);
        assertNotEquals(userSystemId1, userSystemId3);
    }

    @Test
    void testUserSystemIdHashCode() {
        UserSystemId userSystemId1 = UserSystemId.of(123L);
        UserSystemId userSystemId2 = UserSystemId.of(123L);

        assertEquals(userSystemId1.hashCode(), userSystemId2.hashCode());
    }

    @Test
    void testUserSystemIdToString() {
        UserSystemId userSystemId = UserSystemId.of(12345L);
        assertEquals("12345", userSystemId.toString());
    }

    @Test
    void testUserSystemIdValue() {
        Long expectedValue = 42L;
        UserSystemId userSystemId = UserSystemId.of(expectedValue);
        
        assertEquals(expectedValue, userSystemId.value());
    }

    @Test
    void testDifferentUserSystemIdsNotEqual() {
        UserSystemId userSystemId1 = UserSystemId.of(1L);
        UserSystemId userSystemId2 = UserSystemId.of(2L);

        assertNotEquals(userSystemId1, userSystemId2);
        assertNotEquals(userSystemId1.hashCode(), userSystemId2.hashCode());
    }

    @Test
    void testUserSystemIdImmutability() {
        UserSystemId userSystemId = UserSystemId.of(100L);
        Long value = userSystemId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, userSystemId.value());
        assertEquals(100L, userSystemId.value());
    }

    @Test
    void testUserSystemIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        UserSystemId userSystemId = UserSystemId.of(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userSystemId.value());
    }
}
