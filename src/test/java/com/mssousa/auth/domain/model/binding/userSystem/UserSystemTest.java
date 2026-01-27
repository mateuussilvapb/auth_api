package com.mssousa.auth.domain.model.binding.userSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;

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
    private UserId userId;
    private SystemId systemId;

    @BeforeEach
    void setUp() {
        id = UserSystemId.of(1L);
        userId = UserId.of(1L);
        systemId = SystemId.of(1L);
    }

    // ==================== Criação e Getters ====================

    /**
     * Testa a criação de um vínculo usuário-sistema.
     */
    @Test
    void createUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();

        assertNotNull(userSystem);
        assertEquals(id, userSystem.getId());
        assertEquals(userId, userSystem.getUserId());
        assertEquals(systemId, userSystem.getSystemId());
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com usuário nulo.
     */
    @Test
    void createUserSystemWithNullUser() {
        assertThrows(DomainException.class,
                () -> UserSystem.builder()
                    .id(id)
                    .userId(null)
                    .systemId(systemId)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com sistema nulo.
     */
    @Test
    void createUserSystemWithNullSystem() {
        assertThrows(DomainException.class,
                () -> UserSystem.builder()
                    .id(id)
                    .userId(userId)
                    .systemId(null)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com status nulo.
     */
    @Test
    void createUserSystemWithNullStatus() {
        assertThrows(DomainException.class,
                () -> UserSystem.builder()
                    .id(id)
                    .userId(userId)
                    .systemId(systemId)
                    .status(null)
                    .build());
    }

    /**
     * Testa a criação de um vínculo usuário-sistema com ID nulo.
     */
    @Test
    void createUserSystemWithNullId() {
        assertThrows(DomainException.class,
                () -> UserSystem.builder()
                    .id(null)
                    .userId(userId)
                    .systemId(systemId)
                    .status(BindingStatus.ACTIVE)
                    .build());
    }

    /**
     * Testa a obtenção do ID do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemId() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertEquals(id, userSystem.getId());
    }

    /**
     * Testa a obtenção do usuário do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemUser() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertEquals(userId, userSystem.getUserId());
    }

    /**
     * Testa a obtenção do sistema do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertEquals(systemId, userSystem.getSystemId());
    }

    /**
     * Testa a obtenção do status do vínculo usuário-sistema.
     */
    @Test
    void getUserSystemStatus() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Testa a ativação do vínculo usuário-sistema.
     */
    @Test
    void activateUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.INACTIVE)
            .build();
        userSystem.activate();
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a desativação do vínculo usuário-sistema.
     */
    @Test
    void deactivateUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        userSystem.deactivate();
        assertEquals(BindingStatus.INACTIVE, userSystem.getStatus());
    }

    /**
     * Testa a bloqueio do vínculo usuário-sistema.
     */
    @Test
    void blockUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        userSystem.block();
        assertEquals(BindingStatus.BLOCKED, userSystem.getStatus());
    }

    @Test
    void unblockUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.BLOCKED)
            .build();
        userSystem.unblock();
        assertEquals(BindingStatus.ACTIVE, userSystem.getStatus());
    }
    
    @Test
    void unblockNotBlockedUserSystem() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        assertThrows(DomainException.class, userSystem::unblock);
    }

    // ==================== Validação de Acesso ====================
    /**
     * Testa a validação de acesso ao sistema com um vínculo ativo.
     */
    @Test
    void validateUserSystemAccess() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.ACTIVE)
            .build();
        userSystem.validateAccess();
    }

    /**
     * Testa a validação de acesso ao sistema com um vínculo inativo.
     */
    @Test
    void validateUserSystemAccessInactive() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.INACTIVE)
            .build();
        assertThrows(DomainException.class, userSystem::validateAccess);
    }

    /**
     * Testa a validação de acesso ao sistema com um vínculo bloqueado.
     */
    @Test
    void validateUserSystemAccessBlocked() {
        UserSystem userSystem = UserSystem.builder()
            .id(id)
            .userId(userId)
            .systemId(systemId)
            .status(BindingStatus.BLOCKED)
            .build();
        assertThrows(DomainException.class, userSystem::validateAccess);
    }
}