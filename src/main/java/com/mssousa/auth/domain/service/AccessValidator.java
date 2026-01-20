package com.mssousa.auth.domain.service;

import org.springframework.stereotype.Service;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.user.User;

/**
 * Domain Service responsável por validar se um usuário possui acesso a um sistema.
 * 
 * Encapsula as regras de negócio de autorização que não pertencem a uma única entidade.
 */
@Service
public class AccessValidator {

    /**
     * Valida se um usuário pode acessar um sistema específico.
     * 
     * Regras aplicadas:
     * 1. Usuário MASTER tem acesso a todos os sistemas (bypass)
     * 2. Usuário deve estar ATIVO
     * 3. Sistema deve estar ATIVO
     * 4. Deve existir vínculo UserSystem entre usuário e sistema
     * 5. Vínculo deve estar ATIVO
     * 
     * @param user Usuário que está tentando acessar
     * @param system Sistema que está sendo acessado
     * @param userSystem Vínculo entre usuário e sistema (pode ser null se não existir)
     * @throws DomainException se o acesso for negado
     */
    public void validateAccess(User user, ClientSystem system, UserSystem userSystem) {
        // Regra 1: Valida se status do usuário e do sistema estão válidos (independente se usuário é MASTER ou não)
        validateUserStatus(user);
        validateSystemStatus(system);

        // Regra 2: Usuário MASTER tem acesso total
        if (user.isMaster()) {
            // Master bypassa as demais validações
            return; // Acesso permitido
        }

        // Regra 3: Valida existência de vínculo
        if (userSystem == null) {
            throw new DomainException(
                String.format("Usuário '%s' não possui vínculo com o sistema '%s'",
                    user.getUsername().value(),
                    system.getName())
            );
        }

        // Regra 4: Valida status do vínculo
        validateBindingStatus(userSystem, user, system);
    }

    /**
     * Valida se o usuário está em status válido para acesso.
     */
    private void validateUserStatus(User user) {
        if (!user.isActive()) {
            throw new DomainException(
                String.format("Usuário '%s' está inativo ou bloqueado e não pode acessar o sistema",
                    user.getUsername().value())
            );
        }
    }

    /**
     * Valida se o sistema está ativo.
     */
    private void validateSystemStatus(ClientSystem system) {
        if (!system.isActive()) {
            throw new DomainException(
                String.format("Sistema '%s' está inativo", system.getName())
            );
        }
    }

    /**
     * Valida se o vínculo entre usuário e sistema está ativo.
     */
    private void validateBindingStatus(UserSystem userSystem, User user, ClientSystem system) {
        if (!userSystem.isActive()) {
            throw new DomainException(
                String.format("Vínculo do usuário '%s' com o sistema '%s' está inativo ou bloqueado e não pode acessar o sistema",
                    user.getUsername().value(),
                    system.getName())
            );
        }
    }

    /**
     * Verifica se um usuário pode acessar um sistema (retorna boolean).
     * Não lança exceção, apenas retorna true/false.
     * 
     * @param user Usuário
     * @param system Sistema
     * @param userSystem Vínculo (pode ser null)
     * @return true se pode acessar, false caso contrário
     */
    public boolean canAccess(User user, ClientSystem system, UserSystem userSystem) {
        try {
            validateAccess(user, system, userSystem);
            return true;
        } catch (DomainException e) {
            return false;
        }
    }
}
