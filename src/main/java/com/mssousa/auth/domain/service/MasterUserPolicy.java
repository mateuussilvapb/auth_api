package com.mssousa.auth.domain.service;

import org.springframework.stereotype.Service;

import com.mssousa.auth.domain.model.user.User;

/**
 * Domain Service responsável por encapsular as regras de negócio relacionadas
 * a usuários com perfil MASTER.
 * 
 * Usuários MASTER possuem privilégios especiais no sistema:
 * - Acesso a todos os sistemas sem necessidade de vínculo
 * - Não precisam ter roles atribuídos
 * - Podem gerenciar qualquer recurso do Auth Server
 */
@Service
public class MasterUserPolicy {

    /**
     * Verifica se um usuário é MASTER.
     *  
     * @param user Usuário a ser verificado
     * @return true se for MASTER, false caso contrário
     */
    public boolean isMaster(User user) {
        return user.isMaster();
    }

    /**
     * Determina se um usuário precisa de roles para acessar um sistema.
     * 
     * Regra de negócio: Usuários MASTER NÃO precisam de roles.
     * Eles têm acesso total a todos os sistemas por padrão.
     * 
     * @param user Usuário
     * @return false sempre (MASTER não precisa de roles)
     */
    public boolean requiresRoles(User user) {
        if (user.isMaster()) {
            return false; // MASTER não precisa de roles
        }
        return true; // Usuários normais precisam de roles
    }

    /**
     * Determina se um usuário precisa de vínculo com sistema.
     * 
     * Regra de negócio: Usuários MASTER NÃO precisam de vínculo UserSystem.
     * Eles podem acessar qualquer sistema automaticamente.
     * 
     * @param user Usuário
     * @return false se for MASTER, true caso contrário
     */
    public boolean requiresSystemBinding(User user) {
        if (user.isMaster()) {
            return false; // MASTER não precisa de vínculo
        }
        return true; // Usuários normais precisam de vínculo
    }

    /**
     * Verifica se um usuário pode promover outro usuário a MASTER.
     * 
     * Regra de negócio: Apenas usuários MASTER podem criar outros usuários MASTER.
     * 
     * @param currentUser Usuário que está tentando fazer a operação
     * @return true se pode promover, false caso contrário
     */
    public boolean canPromoteToMaster(User currentUser) {
        return currentUser.isMaster();
    }

    /**
     * Verifica se um usuário pode rebaixar outro usuário de MASTER.
     * 
     * Regra de negócio: Apenas usuários MASTER podem rebaixar outros MASTER.
     * 
     * @param currentUser Usuário que está tentando fazer a operação
     * @return true se pode rebaixar, false caso contrário
     */
    public boolean canDemoteFromMaster(User currentUser) {
        return currentUser.isMaster();
    }

    /**
     * Valida se pode modificar o status MASTER de um usuário.
     * 
     * Regra de negócio: Apenas MASTER pode alterar o status MASTER de qualquer usuário.
     * 
     * @param currentUser Usuário que está executando a ação
     * @param targetUser Usuário que será modificado
     * @param newMasterStatus Novo status MASTER
     * @return true se a operação é permitida, false caso contrário
     */
    public boolean canChangeMasterStatus(User currentUser, User targetUser, boolean newMasterStatus) {
        // Apenas MASTER pode alterar status MASTER
        if (!currentUser.isMaster()) {
            return false;
        }

        // MASTER pode alterar qualquer usuário (inclusive ele mesmo, se necessário)
        return true;
    }

    /**
     * Obtém as permissões implícitas de um usuário MASTER.
     * 
     * @param user Usuário
     * @return String representando as permissões (empty se não for MASTER)
     */
    public String getImplicitPermissions(User user) {
        if (user.isMaster()) {
            return "FULL_ACCESS"; // MASTER tem acesso total
        }
        return ""; // Usuários normais não têm permissões implícitas
    }
}
