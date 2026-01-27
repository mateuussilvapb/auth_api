package com.mssousa.auth.domain.model.binding.userSystemRole;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.role.SystemRoleId;

/**
 * Testes unitários da entidade de domínio {@link UserSystemRole}.
 */
class UserSystemRoleTest {

    private UserSystemRoleId id;
    private UserSystemId userSystemId;
    private SystemRoleId systemRoleId;
    
    @BeforeEach
    void setUp() {
        id = UserSystemRoleId.of(1L);
        userSystemId = UserSystemId.of(1L);
        systemRoleId = SystemRoleId.of(1L);
    }

    // ==================== Criação e Getters ====================

    @Test
    void createUserSystemRole() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.ACTIVE)
            .build();

        assertNotNull(userSystemRole);
        assertEquals(id, userSystemRole.getId());
        assertEquals(userSystemId, userSystemRole.getUserSystemId());
        assertEquals(systemRoleId, userSystemRole.getSystemRoleId());
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void createUserSystemRoleWithNullId() {
        assertThrows(DomainException.class,
                () -> UserSystemRole.builder()
                    .userSystemId(userSystemId)
                    .systemRoleId(systemRoleId)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    @Test
    void createUserSystemRoleWithNullUserSystem() {
        assertThrows(DomainException.class,
                () -> UserSystemRole.builder()
                    .id(id)
                    .systemRoleId(systemRoleId)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    @Test
    void createUserSystemRoleWithNullRole() {
        assertThrows(DomainException.class,
                () -> UserSystemRole.builder()
                    .id(id)
                    .userSystemId(userSystemId)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    @Test
    void createUserSystemRoleWithNullStatus() {
        assertThrows(DomainException.class,
                () -> UserSystemRole.builder()
                    .id(id)
                    .userSystemId(userSystemId)
                    .systemRoleId(systemRoleId)
                    .build());
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void activateUserSystemRole() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.INACTIVE)
            .build();
        userSystemRole.activate();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void deactivateUserSystemRole() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.ACTIVE)
            .build();
        userSystemRole.deactivate();
        assertEquals(BindingStatus.INACTIVE, userSystemRole.getStatus());
    }

    @Test
    void blockUserSystemRole() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.ACTIVE)
            .build();
        userSystemRole.block();
        assertEquals(BindingStatus.BLOCKED, userSystemRole.getStatus());
    }

    @Test
    void unblockUserSystemRole() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.BLOCKED)
            .build();
        userSystemRole.unblock();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    // ==================== Validação de Acesso ====================

    @Test
    void validateAccessSuccess() {
        // Tudo ativo (padrão do setUp)
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertDoesNotThrow(userSystemRole::validateAccess);
    }

    @Test
    void validateAccessInactiveBinding() {
        UserSystemRole userSystemRole = UserSystemRole.builder()
            .id(id)
            .userSystemId(userSystemId)
            .systemRoleId(systemRoleId)
            .status(BindingStatus.INACTIVE)
            .build();
        DomainException ex = assertThrows(DomainException.class, userSystemRole::validateAccess);
        assertEquals("Perfil não está ativo para este usuário no sistema", ex.getMessage());
    }
}
