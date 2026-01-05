package com.mssousa.auth.domain.model.system;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemIdTest {

    @Test
    void testValidSystemId() {
        SystemId systemId = new SystemId(1L);
        assertEquals(1L, systemId.value());
    }

    @Test
    void testValidSystemIdLargeNumber() {
        SystemId systemId = new SystemId(999999999L);
        assertEquals(999999999L, systemId.value());
    }

    @Test
    void testInvalidSystemIdNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new SystemId(null));
        assertNotNull(exception);
    }

    @Test
    void testInvalidSystemIdZero() {
        DomainException exception = assertThrows(DomainException.class, () -> new SystemId(0L));
        assertEquals("SystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidSystemIdNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new SystemId(-1L));
        assertEquals("SystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testInvalidSystemIdLargeNegative() {
        DomainException exception = assertThrows(DomainException.class, () -> new SystemId(-999999L));
        assertEquals("SystemId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void testSystemIdEquality() {
        SystemId systemId1 = new SystemId(123L);
        SystemId systemId2 = new SystemId(123L);
        SystemId systemId3 = new SystemId(456L);

        assertEquals(systemId1, systemId2);
        assertNotEquals(systemId1, systemId3);
    }

    @Test
    void testSystemIdHashCode() {
        SystemId systemId1 = new SystemId(123L);
        SystemId systemId2 = new SystemId(123L);

        assertEquals(systemId1.hashCode(), systemId2.hashCode());
    }

    @Test
    void testSystemIdToString() {
        SystemId systemId = new SystemId(12345L);
        assertEquals("12345", systemId.toString());
    }

    @Test
    void testSystemIdValue() {
        Long expectedValue = 42L;
        SystemId systemId = new SystemId(expectedValue);
        
        assertEquals(expectedValue, systemId.value());
    }

    @Test
    void testDifferentSystemIdsNotEqual() {
        SystemId systemId1 = new SystemId(1L);
        SystemId systemId2 = new SystemId(2L);

        assertNotEquals(systemId1, systemId2);
        assertNotEquals(systemId1.hashCode(), systemId2.hashCode());
    }

    @Test
    void testSystemIdImmutability() {
        SystemId systemId = new SystemId(100L);
        Long value = systemId.value();
        
        // O valor retornado deve ser o mesmo sempre
        assertEquals(value, systemId.value());
        assertEquals(100L, systemId.value());
    }

    @Test
    void testSystemIdWithMaxLong() {
        // Testar com o maior valor possível de Long
        SystemId systemId = new SystemId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, systemId.value());
    }
}
