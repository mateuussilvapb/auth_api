package com.mssousa.auth.domain.model.binding.userSystemRole;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.model.role.SystemRoleStatus;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.system.SystemStatus;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;

/**
 * Testes unitários da entidade de domínio {@link UserSystemRole}.
 */
class UserSystemRoleTest {

    private UserSystemRoleId id;
    private UserSystem userSystem;
    private SystemRole role;
    
    // Auxiliares para construção
    private User user;
    private ClientSystem system;

    @BeforeEach
    void setUp() {
        id = new UserSystemRoleId(1L);
        
        // Setup UserSystem
        user = new User(
                new UserId(1L),
                new Username("testuser"),
                new Email("test@example.com"),
                Password.fromPlainText("password123"),
                false,
                UserStatus.ACTIVE,
                "Test User");
        
        system = new ClientSystem(
                new SystemId(1L),
                "Test System",
                "test@example.com",
                "password123",
                "http://localhost:8080",
                SystemStatus.ACTIVE);
                
        userSystem = new UserSystem(
                new UserSystemId(1L),
                user,
                system,
                BindingStatus.ACTIVE
        );

        // Setup SystemRole
        role = new SystemRole(
                new SystemRoleId(1L),
                new SystemId(1L),
                "ADMIN",
                "Administrator Role",
                SystemRoleStatus.ACTIVE
        );
    }

    // ==================== Criação e Getters ====================

    @Test
    void createUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);

        assertNotNull(userSystemRole);
        assertEquals(id, userSystemRole.getId());
        assertEquals(userSystem, userSystemRole.getUserSystem());
        assertEquals(role, userSystemRole.getRole());
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void createUserSystemRoleWithNullId() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(null, userSystem, role, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullUserSystem() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, null, role, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullRole() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, userSystem, null, BindingStatus.ACTIVE));
    }

    @Test
    void createUserSystemRoleWithNullStatus() {
        assertThrows(DomainException.class,
                () -> new UserSystemRole(id, userSystem, role, null));
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void activateUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.INACTIVE);
        userSystemRole.activate();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void deactivateUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        userSystemRole.deactivate();
        assertEquals(BindingStatus.INACTIVE, userSystemRole.getStatus());
    }

    @Test
    void blockUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        userSystemRole.block();
        assertEquals(BindingStatus.BLOCKED, userSystemRole.getStatus());
    }

    @Test
    void unblockUserSystemRole() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.BLOCKED);
        userSystemRole.unblock();
        assertEquals(BindingStatus.ACTIVE, userSystemRole.getStatus());
    }

    @Test
    void activateAlreadyActive() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystemRole::activate);
    }

    @Test
    void deactivateAlreadyInactive() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.INACTIVE);
        assertThrows(DomainException.class, userSystemRole::deactivate);
    }

    @Test
    void blockAlreadyBlocked() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.BLOCKED);
        assertThrows(DomainException.class, userSystemRole::block);
    }

    @Test
    void unblockNotBlocked() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystemRole::unblock);
    }

    // ==================== Validação de Acesso ====================

    @Test
    void validateAccessSuccess() {
        // Tudo ativo (padrão do setUp)
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        assertDoesNotThrow(userSystemRole::validateAccess);
    }

    @Test
    void validateAccessInactiveBinding() {
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.INACTIVE);
        DomainException ex = assertThrows(DomainException.class, userSystemRole::validateAccess);
        assertEquals("Perfil não está ativo para este usuário no sistema", ex.getMessage());
    }

    @Test
    void validateAccessInactiveUserSystem() {
        userSystem.deactivate(); // Desativa o vínculo pai
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);
        
        // Deve falhar na validação do userSystem
        assertThrows(DomainException.class, userSystemRole::validateAccess);
    }

    @Test
    void validateAccessInactiveRole() {
        role.deactivate(); // Desativa a role
        UserSystemRole userSystemRole = new UserSystemRole(id, userSystem, role, BindingStatus.ACTIVE);

        DomainException ex = assertThrows(DomainException.class, userSystemRole::validateAccess);
        assertEquals("O Perfil (Role) está inativo e não pode conceder acesso", ex.getMessage());
    }
}
