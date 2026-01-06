package com.mssousa.auth.domain.model.role;

/**
 * Enum representando o status de um perfil no Auth Server.
 */
public enum SystemRoleStatus {
    /**
     * Perfil ativo e disponível para autenticação.
     */
    ACTIVE,
    
    /**
     * Perfil inativo, não aceita novas autenticações.
     */
    INACTIVE
}
