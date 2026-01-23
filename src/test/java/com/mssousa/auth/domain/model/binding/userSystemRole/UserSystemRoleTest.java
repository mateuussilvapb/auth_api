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
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);

        assertNotNull(userSystemRole);
        assertEquals(id, userSystemRole.getId());
        assertEquals(userSystemId, userSystemRole.getUserSystemId());
        assertEquals(systemRoleId, userSystemRole.getSystemRoleId());
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void createUserSystemRoleWithNullId() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(null, userSystemId, systemRoleId, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullUserSystem() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, null, systemRoleId, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullRole() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, userSystemId, null, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullStatus() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, userSystemId, systemRoleId, null));
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void activateUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.INACTIVE);
        userSystemRole.activate();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void deactivateUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);
        userSystemRole.deactivate();
        assertEquals(BindingStatus.INACTIVE, userSystemRole.getStatus());
    }

    @Test
    void blockUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);
        userSystemRole.block();
        assertEquals(BindingStatus.BLOCKED, userSystemRole.getStatus());
    }

    @Test
    void unblockUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.BLOCKED);
        userSystemRole.unblock();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void activateAlreadyActive() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystemRole::activate);
    }

    @Test
    void deactivateAlreadyInactive() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.INACTIVE);
        assertThrows(DomainException.class, userSystemRole::deactivate);
    }

    @Test
    void blockAlreadyBlocked() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.BLOCKED);
        assertThrows(DomainException.class, userSystemRole::block);
    }

    @Test
    void unblockNotBlocked() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystemRole::unblock);
    }

    // ==================== Validação de Acesso ====================

    @Test
    void validateAccessSuccess() {
        // Tudo ativo (padrão do setUp)
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.ACTIVE);
        assertDoesNotThrow(userSystemRole::validateAccess);
    }

    @Test
    void validateAccessInactiveBinding() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystemId, systemRoleId, BindingStatus.INACTIVE);
        DomainException ex = assertThrows(DomainException.class, userSystemRole::validateAccess);
        assertEquals("Perfil não está ativo para este usuário no sistema", ex.getMessage());
    }
}
