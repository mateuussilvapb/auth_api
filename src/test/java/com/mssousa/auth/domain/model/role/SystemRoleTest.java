package com.mssousa.auth.domain.model.role;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;

/**
 * Testes unitários da entidade de domínio {@link SystemRole}.
 *
 * O foco destes testes é:
 * - Garantir as invariantes do agregado
 * - Validar regras de transição de estado
 * - Proteger o domínio contra estados inválidos
 *
 * NÃO testamos getters isoladamente, pois eles não representam
 * comportamento de domínio.
 */
class SystemRoleTest {

    private SystemRoleId id;
    private SystemId systemId;
    private String code;
    private String description;

    @BeforeEach
    void setUp() {
        this.id = SystemRoleId.of(1L);
        this.systemId = SystemId.of(1L);
        this.code = "ADMIN";
        this.description = "Perfil administrativo";
    }

    // =====================================================
    // Criação do agregado
    // =====================================================

    @Test
    void shouldCreateSystemRoleWhenAllParametersAreValid() {
        SystemRole role = SystemRole.builder()
                .id(id)
                .system_id(systemId)
                .code(code)
                .description(description)
                .status(SystemRoleStatus.ACTIVE)
                .build();

        assertNotNull(role);
        assertEquals(id, role.getId());
        assertEquals(systemId, role.getSystemId());
        assertEquals(code, role.getCode());
        assertEquals(description, role.getDescription());
        assertEquals(SystemRoleStatus.ACTIVE, role.getStatus());
        assertTrue(role.isActive());
    }

    // =====================================================
    // Validações de invariantes (construtor)
    // =====================================================

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> SystemRole.builder()
                        .id(null)
                        .system_id(systemId)
                        .code(code)
                        .description(description)
                        .status(SystemRoleStatus.ACTIVE)
                        .build());

        assertEquals("ID do perfil não pode ser nulo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> SystemRole.builder()
                        .id(id)
                        .system_id(systemId)
                        .code(null)
                        .description(description)
                        .status(SystemRoleStatus.ACTIVE)
                        .build());

        assertEquals("Código do perfil não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> SystemRole.builder()
                        .id(id)
                        .system_id(systemId)
                        .code("   ")
                        .description(description)
                        .status(SystemRoleStatus.ACTIVE)
                        .build());

        assertEquals("Código do perfil não pode ser nulo ou vazio", exception.getMessage());
    }

    // =====================================================
    // Regras de transição de estado
    // =====================================================

    @Test
    void shouldActivateInactiveRole() {
        SystemRole role = SystemRole.builder()
                .id(id)
                .system_id(systemId)
                .code(code)
                .description(description)
                .status(SystemRoleStatus.INACTIVE)
                .build();

        role.activate();

        assertTrue(role.isActive());
        assertEquals(SystemRoleStatus.ACTIVE, role.getStatus());
    }

    @Test
    void shouldDeactivateActiveRole() {
        SystemRole role = SystemRole.builder()
                .id(id)
                .system_id(systemId)
                .code(code)
                .description(description)
                .status(SystemRoleStatus.ACTIVE)
                .build();

        role.deactivate();

        assertTrue(role.isInactive());
        assertEquals(SystemRoleStatus.INACTIVE, role.getStatus());
    }

    // =====================================================
    // Atualização de estado mutável
    // =====================================================

    @Test
    void shouldUpdateDescriptionWhenNewDescriptionIsValid() {
        SystemRole role = SystemRole.builder()
                .id(id)
                .system_id(systemId)
                .code(code)
                .description(description)
                .status(SystemRoleStatus.ACTIVE)
                .build();

        role.updateDescription("Nova descrição");

        assertEquals("Nova descrição", role.getDescription());
    }
}
