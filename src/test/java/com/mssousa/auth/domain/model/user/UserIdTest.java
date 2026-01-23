package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void testValidUserId() {
        UserId userId = UserId.of(1L);
        assertEquals(1L, userId.value());
    }

    @Test
    void testValidUserIdLargeNumber() {
        UserId userId = UserId.of(999999999L);
        assertEquals(999999999L, userId.value());
    }

    @Test
    void testInvalidUserIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> UserId.of(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidUserIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> UserId.of(0L));
        assertEquals("UserId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserId.of(-1L));
        assertEquals("UserId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidUserIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> UserId.of(-999999L));
        assertEquals("UserId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testUserIdEquality() {
        UserId userId1 = UserId.of(123L);
        UserId userId2 = UserId.of(123L);
        UserId userId3 = UserId.of(456L);

        assertEquals(userId1, userId2);
        assertNotEquals(userId1, userId3);
    }

    @Test
    void testUserIdHashCode() {
        UserId userId1 = UserId.of(123L);
        UserId userId2 = UserId.of(123L);

        assertEquals(userId1.hashCode(), userId2.hashCode());
    }

    @Test
    void testUserIdToString() {
        UserId userId = UserId.of(12345L);
        assertEquals("12345", userId.toString());
    }

    @Test
    void testUserIdValue() {
        Long expectedValue = 42L;
        UserId userId = UserId.of(expectedValue);
        
        assertEquals(expectedValue, userId.value());
    }

    @Test
    void testDifferentUserIdsNotEqual() {
        UserId userId1 = UserId.of(1L);
        UserId userId2 = UserId.of(2L);

        assertNotEquals(userId1, userId2);
        assertNotEquals(userId1.hashCode(), userId2.hashCode());
    }

    @Test
    void testUserIdImmutability() {
        UserId userId = UserId.of(100L);
        Long value = userId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, userId.value());
        assertEquals(100L, userId.value());
    }

    @Test
    void testUserIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        UserId userId = UserId.of(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, userId.value());
    }
}
