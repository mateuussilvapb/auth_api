package com.mssousa.auth.domain.model.system;

/**
 * Enum representando o status de um sistema cliente.
 */
public enum SystemStatus {
    /**
     * Sistema ativo e disponível para autenticação.
     */
    ACTIVE,
    
    /**
     * Sistema inativo, não aceita novas autenticações.
     */
    INACTIVE
}
