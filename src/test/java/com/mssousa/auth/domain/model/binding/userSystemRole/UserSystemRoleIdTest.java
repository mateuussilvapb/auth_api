package com.mssousa.auth.domain.model.binding.userSystemRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;

class UserSystemRoleIdTest {
    
     @Test
    void testValiduserSystemRoleId() {
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(1L);
        assertEquals(1L, userSystemRoleId.value());
    }

    @Test
    void testValidUserSystemRoleIdLargeNumber() {
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(999999999L);
        assertEquals(999999999L, userSystemRoleId.value());
    }

    @Test
    void testInvalidUserSystemRoleIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> UserSystemRoleId.of(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidUserSystemRoleIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemRoleId.of(0L));
        assertEquals("UserSystemRoleId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemRoleIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemRoleId.of(-1L));
        assertEquals("UserSystemRoleId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserSystemRoleIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserSystemRoleId.of(-999999L));
        assertEquals("UserSystemRoleId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testUserSystemRoleIdEquality() {
        UserSystemRoleId userSystemRoleId1 = UserSystemRoleId.of(123L);
        UserSystemRoleId userSystemRoleId2 = UserSystemRoleId.of(123L);
        UserSystemRoleId userSystemRoleId3 = UserSystemRoleId.of(456L);

        assertEquals(userSystemRoleId1, userSystemRoleId2);
        assertNotEquals(userSystemRoleId1, userSystemRoleId3);
    }

    @Test
    void testUserSystemRoleIdHashCode() {
        UserSystemRoleId userSystemRoleId1 = UserSystemRoleId.of(123L);
        UserSystemRoleId userSystemRoleId2 = UserSystemRoleId.of(123L);

        assertEquals(userSystemRoleId1.hashCode(), userSystemRoleId2.hashCode());
    }

    @Test
    void testUserSystemRoleIdToString() {
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(12345L);
        assertEquals("12345", userSystemRoleId.toString());
    }

    @Test
    void testUserSystemRoleIdValue() {
        Long expectedValue = 42L;
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(expectedValue);
        
        assertEquals(expectedValue, userSystemRoleId.value());
    }

    @Test
    void testDifferentUserSystemRoleIdsNotEqual() {
        UserSystemRoleId userSystemRoleId1 = UserSystemRoleId.of(1L);
        UserSystemRoleId userSystemRoleId2 = UserSystemRoleId.of(2L);

        assertNotEquals(userSystemRoleId1, userSystemRoleId2);
        assertNotEquals(userSystemRoleId1.hashCode(), userSystemRoleId2.hashCode());
    }

    @Test
    void testUserSystemRoleIdImmutability() {
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(100L);
        Long value = userSystemRoleId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, userSystemRoleId.value());
        assertEquals(100L, userSystemRoleId.value());
    }

    @Test
    void testUserSystemRoleIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        UserSystemRoleId userSystemRoleId = UserSystemRoleId.of(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userSystemRoleId.value());
    }
}
