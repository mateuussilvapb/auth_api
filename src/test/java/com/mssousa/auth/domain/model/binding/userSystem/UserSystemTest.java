package com.mssousa.auth.domain.model.binding.userSystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
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
 * Testes unitários da entidade de domínio {@link UserSystem}.
 *
 * O foco destes testes é:
 * - Garantir as invariantes do agregado
 * - Validar regras de transição de estado
 * - Proteger o domínio contra estados inválidos
 *
 */
class UserSystemTest {
    private UserSystemId id;
    private User user;
    private ClientSystem system;

    @BeforeEach
    void setUp() {
        id = new UserSystemId(1L);
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
    }

    // ==================== Criação e Getters ====================

    /**
     * Testa a criação de um vínculo usuário-sistema.
     */
    @Test
    void createUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);

        assertNotNull(userSystem);
        assertEquals(id, userSystem.getId());
        assertEquals(user, userSystem.getUser());
        assertEquals(system, userSystem.getSystem());
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com usuário nulo.
     */
    @Test
    void createUserSystemWithNullUser() {
        assertThrows(DomainException.class,
                () -> new UserSystem(id, null, system, BindingStatus.ACTIVE));
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com sistema nulo.
     */
    @Test
    void createUserSystemWithNullSystem() {
        assertThrows(DomainException.class,
                () -> new UserSystem(id, user, null, BindingStatus.ACTIVE));
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com status nulo.
     */
    @Test
    void createUserSystemWithNullStatus() {
        assertThrows(DomainException.class,
                () -> new UserSystem(id, user, system, null));
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com ID nulo.
     */
    @Test
    void createUserSystemWithNullId() {
        assertThrows(DomainException.class,
                () -> new UserSystem(null, user, system, BindingStatus.ACTIVE));
    }

    /**
     * Testa a obtenção do ID do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemId() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertEquals(id, userSystem.getId());
    }

    /**
     * Testa a obtenção do usuário do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemUser() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertEquals(user, userSystem.getUser());
    }

    /**
     * Testa a obtenção do sistema do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertEquals(system, userSystem.getSystem());
    }

    /**
     * Testa a obtenção do status do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemStatus() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Testa a ativação do vínculo usuário-sistema.
     */
    @Test
    void activateUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.INACTIVE);
        userSystem.activate();
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a desativação do vínculo usuário-sistema.
     */
    @Test
    void deactivateUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        userSystem.deactivate();
        assertEquals(BindingStatus.INACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a bloqueio do vínculo usuário-sistema.
     */
    @Test
    void blockUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        userSystem.block();
        assertEquals(BindingStatus.BLOCKED, userSystem.getStatus());
    }

    @Test
    void unblockUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.BLOCKED);
        userSystem.unblock();
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }
    
    @Test
    void unblockNotBlockedUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystem::unblock);
    }
        

    /**
     * Testa a ativação de um vínculo já ativo.
     */
    @Test
    void activateAlreadyActiveUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        assertThrows(DomainException.class, userSystem::activate);
    }

    /**
     * Testa a desativação de um vínculo já inativo.
     */
    @Test
    void deactivateAlreadyInactiveUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.INACTIVE);
        assertThrows(DomainException.class, userSystem::deactivate);
    }

    /**
     * Testa a bloqueio de um vínculo já bloqueado.
     */
    @Test
    void blockAlreadyBlockedUserSystem() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.BLOCKED);
        assertThrows(DomainException.class, userSystem::block);
    }

    // ==================== Validação de Acesso ====================
    /**
     * Testa a validação de acesso ao sistema com um vínculo ativo.
     */
    @Test
    void validateUserSystemAccess() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        userSystem.validateAccess();
    }

    /**
     * Testa a validação de acesso ao sistema com um vínculo inativo.
     */
    @Test
    void validateUserSystemAccessInactive() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.INACTIVE);
        assertThrows(DomainException.class, userSystem::validateAccess);
    }

    /**
     * Testa a validação de acesso ao sistema com um vínculo bloqueado.
     */
    @Test
    void validateUserSystemAccessBlocked() {
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.BLOCKED);
        assertThrows(DomainException.class, userSystem::validateAccess);
    }
    
    @Test
    void validateAccessInactiveUser() {
        user.disable();
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        DomainException ex = assertThrows(DomainException.class, userSystem::validateAccess);
        assertEquals("Usuário não está ativo", ex.getMessage());
    }

    @Test
    void validateAccessInactiveSystem() {
        system.deactivate();
        UserSystem userSystem = new UserSystem(id, user, system, BindingStatus.ACTIVE);
        DomainException ex = assertThrows(DomainException.class, userSystem::validateAccess);
        assertEquals("Sistema não está ativo", ex.getMessage());
    }
}