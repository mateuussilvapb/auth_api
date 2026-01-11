package com.mssousa.auth.domain.model.role;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;

/**
 * Entidade de domínio representando um Perfil no Auth Server.
 * <p>
 * Um perfil é uma categoria de usuário que possui permissões específicas.
 * Cada perfil possui identificadores OAuth2, URI de redirecionamento e status.
 * </p>
 */
public class SystemRole {
    private final SystemRoleId id;
    private final SystemId system_id;
    private final String code;
    private String description;
    private SystemRoleStatus status;

    /**
     * Cria um novo perfil.
     *
     * @param id identificador único do perfil
     * @param system_id identificador do sistema
     * @param code código do perfil
     * @param description descrição do perfil
     * @param status status do perfil
     */
    public SystemRole(SystemRoleId id, SystemId system_id, String code, String description, SystemRoleStatus status) {
        validateId(id);
        validateSystemId(system_id);
        validateCode(code);
        validateDescription(description);
        validateStatus(status);

        this.id = id;
        this.system_id = system_id;
        this.code = code;
        this.description = description;
        this.status = status;
    }

    // ==================== Getters ====================

    public SystemRoleId getId() {
        return id;
    }

    public SystemId getSystemId() {
        return system_id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public SystemRoleStatus getStatus() {
        return status;
    }

    // ==================== Validações ====================

    private void validateId(SystemRoleId id) {
        if (id == null) {
            throw new DomainException("ID do perfil não pode ser nulo");
        }
    }

    private void validateSystemId(SystemId system_id) {
        if (system_id == null) {
            throw new DomainException("ID do sistema não pode ser nulo");
        }
    }

    private void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new DomainException("Código do perfil não pode ser nulo ou vazio");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new DomainException("Descrição do perfil não pode ser nula");
        }
    }

    private void validateStatus(SystemRoleStatus status) {
        if (status == null) {
            throw new DomainException("Status do perfil não pode ser nulo");
        }
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Ativa o perfil, permitindo que ele seja atribuído a usuários.
     */
    public void activate() {
        this.status = SystemRoleStatus.ACTIVE;
    }

    /**
     * Desativa o perfil, impedindo que ele seja atribuído a usuários.
     *
     * @throws DomainException se o perfil já estiver inativo
     */
    public void deactivate() {
        if (this.status == SystemRoleStatus.INACTIVE) {
            throw new DomainException("Perfil já está inativo");
        }
        this.status = SystemRoleStatus.INACTIVE;
    }

    /**
     * Verifica se o perfil está ativo
     */
    public boolean isActive() {
        return this.status == SystemRoleStatus.ACTIVE;
    }

    /**
     * Verifica se o perfil está inativo
     */
    public boolean isInactive() {
        return this.status == SystemRoleStatus.INACTIVE;
    }

    // ==================== Gerenciamento de Configurações ====================

    /**
     * Atualiza a descrição do perfil.
     *
     * @param newDescription nova descrição
     * @throws DomainException se a descrição for nula
     */
    public void updateDescription(String newDescription) {
        validateDescription(newDescription);
        this.description = newDescription;
    }

    // ==================== Verificação ====================
    /**
     * Verifica se uma role pertence a um sistema pelo id do sistema
     * 
     * @param systemId id do sistema para verificação
     * @return true se a role pertencer ao sistema, false caso contrário
     */
    public boolean belongsTo(SystemId systemId) {
        return this.system_id.equals(systemId);
    }
}
