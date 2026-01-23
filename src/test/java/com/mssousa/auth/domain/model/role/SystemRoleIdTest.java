package com.mssousa.auth.domain.model.role;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;

/**
 * Testes unitários do Value Object {@link SystemRoleId}.
 *
 * O foco destes testes é garantir:
 * - Proteção das invariantes do identificador
 * - Correto comportamento de igualdade
 * - Alinhamento com o contrato herdado de {@link DomainId}
 *
 * Value Objects devem ser pequenos, explícitos e altamente confiáveis.
 */
class SystemRoleIdTest {

    // =====================================================
    // Criação válida
    // =====================================================

    @Test
    void shouldCreateSystemRoleIdWhenValueIsPositive() {
        SystemRoleId id = SystemRoleId.of(1L);

        assertNotNull(id);
        assertEquals(1L, id.value());
    }

    // =====================================================
    // Validações de invariantes
    // =====================================================

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> SystemRoleId.of(0L)
        );

        assertEquals("SystemRoleId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> SystemRoleId.of(-1L)
        );

        assertEquals("SystemRoleId deve ser um número positivo", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenValueIsNull() {
        /*
         * A validação de null ocorre na classe base {@link DomainId}
         * via Objects.requireNonNull(value).
         *
         * Este teste documenta explicitamente essa decisão de design.
         */
        assertThrows(
                NullPointerException.class,
                () -> SystemRoleId.of(null)
        );
    }

    // =====================================================
    // Contrato de igualdade (Value Object)
    // =====================================================

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        SystemRoleId id1 = SystemRoleId.of(10L);
        SystemRoleId id2 = SystemRoleId.of(10L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        SystemRoleId id1 = SystemRoleId.of(10L);
        SystemRoleId id2 = SystemRoleId.of(20L);

        assertNotEquals(id1, id2);
    }

    @Test
    void shouldNotBeEqualToDifferentClassEvenWithSameValue() {
        SystemRoleId id = SystemRoleId.of(1L);

        /*
         * Como DomainId utiliza getClass() no equals,
         * IDs de tipos diferentes nunca são iguais,
         * mesmo que o valor interno seja o mesmo.
         */
        assertNotEquals(id, 1L);
    }

    // =====================================================
    // Representação textual
    // =====================================================

    @Test
    void shouldReturnStringRepresentationOfValue() {
        SystemRoleId id = SystemRoleId.of(99L);

        assertEquals("99", id.toString());
    }
}
