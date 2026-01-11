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
        UserSystemId userSystemId = new UserSystemId(1L);
        assertEquals(1L, userSystemId.value());
    }

    @Test
    void testValidUserSystemIdLargeNumber() {
        UserSystemId userSystemId = new UserSystemId(999999999L);
        assertEquals(999999999L, userSystemId.value());
    }

    @Test
    void testInvalidUserSystemIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new UserSystemId(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidUserSystemIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> new UserSystemId(0L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new UserSystemId(-1L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new UserSystemId(-999999L));
        assertEquals("UserSystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testUserSystemIdEquality() {
        UserSystemId userSystemId1 = new UserSystemId(123L);
        UserSystemId userSystemId2 = new UserSystemId(123L);
        UserSystemId userSystemId3 = new UserSystemId(456L);

        assertEquals(userSystemId1, userSystemId2);
        assertNotEquals(userSystemId1, userSystemId3);
    }

    @Test
    void testUserSystemIdHashCode() {
        UserSystemId userSystemId1 = new UserSystemId(123L);
        UserSystemId userSystemId2 = new UserSystemId(123L);

        assertEquals(userSystemId1.hashCode(), userSystemId2.hashCode());
    }

    @Test
    void testUserSystemIdToString() {
        UserSystemId userSystemId = new UserSystemId(12345L);
        assertEquals("12345", userSystemId.toString());
    }

    @Test
    void testUserSystemIdValue() {
        Long expectedValue = 42L;
        UserSystemId userSystemId = new UserSystemId(expectedValue);
        
        assertEquals(expectedValue, userSystemId.value());
    }

    @Test
    void testDifferentUserSystemIdsNotEqual() {
        UserSystemId userSystemId1 = new UserSystemId(1L);
        UserSystemId userSystemId2 = new UserSystemId(2L);

        assertNotEquals(userSystemId1, userSystemId2);
        assertNotEquals(userSystemId1.hashCode(), userSystemId2.hashCode());
    }

    @Test
    void testUserSystemIdImmutability() {
        UserSystemId userSystemId = new UserSystemId(100L);
        Long value = userSystemId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, userSystemId.value());
        assertEquals(100L, userSystemId.value());
    }

    @Test
    void testUserSystemIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        UserSystemId userSystemId = new UserSystemId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userSystemId.value());
    }
}
