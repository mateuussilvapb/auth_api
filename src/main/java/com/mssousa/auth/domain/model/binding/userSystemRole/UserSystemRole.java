package com.mssousa.auth.domain.model.binding.userSystemRole;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.role.SystemRoleId;

/**
 * Entidade de domínio representando um vínculo usuário-perfil no Auth Server.
 * <p>
 * Um vínculo usuário-perfil é um vínculo entre um usuário e um perfil.
 * Cada vínculo possui um status.
 * </p>
 */
public class UserSystemRole {

    private final UserSystemRoleId id;
    private final UserSystemId userSystemId;
    private final SystemRoleId systemRoleId;

    private BindingStatus status;

    /**
     * Cria um novo vínculo usuário-perfil.
     * 
     * @param id
     * @param userSystem
     * @param role
     * @param status
     */
    public UserSystemRole(
            UserSystemRoleId id,
            UserSystemId userSystemId,
            SystemRoleId systemRoleId,
            BindingStatus status
    ) {
        validateId(id);
        validateUserSystemId(userSystemId);
        validateRoleId(systemRoleId);
        validateStatus(status);

        this.id = id;
        this.userSystemId = userSystemId;
        this.systemRoleId = systemRoleId;
        this.status = status;
    }

    // ==================== Getters ====================

    public UserSystemRoleId getId() {
        return id;
    }

    public UserSystemId getUserSystemId() {
        return userSystemId;
    }

    public SystemRoleId getSystemRoleId() {
        return systemRoleId;
    }

    public BindingStatus getStatus() {
        return status;
    }

    // ==================== Validações ====================

    private void validateId(UserSystemRoleId id) {
        if (id == null) {
            throw new DomainException("UserSystemRoleId é obrigatório no vínculo UserSystemRole");
        }
    }

    private void validateUserSystemId(UserSystemId userSystemId) {
        if (userSystemId == null) {
            throw new DomainException("UserSystemId é obrigatório no vínculo UserSystemRole");
        }
    }

    private void validateRoleId(SystemRoleId systemRoleId) {
        if (systemRoleId == null) {
            throw new DomainException("SystemRoleId é obrigatório no vínculo UserSystemRole");
        }
    }

    private void validateStatus(BindingStatus status) {
        if (status == null) {
            throw new DomainException("Status do vínculo UserSystemRole não pode ser nulo");
        }
    }

    
    /**
     * Valida se o vínculo está ativo
     * 
     * @throws DomainException se o vínculo não estiver ativo
     */
    public void validateAccess() {
        if (!isActive()) {
            throw new DomainException("Perfil não está ativo para este usuário no sistema");
        }
    }

    // ==================== Verificação ====================

    /**
     * Verifica se o vínculo está ativo
     * 
     * @return true se o vínculo estiver ativo, false caso contrário
     */
    public boolean isActive() {
        return this.status == BindingStatus.ACTIVE;
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Ativa o vínculo
     * 
     * @throws DomainException se o vínculo já estiver ativo
     */
    public void activate() {
        if (this.status == BindingStatus.ACTIVE) {
            throw new DomainException("Vínculo de perfil já está ativo");
        }
        this.status = BindingStatus.ACTIVE;
    }

    /**
     * Desativa o vínculo
     * 
     * @throws DomainException se o vínculo já estiver inativo
     */
    public void deactivate() {
        if (this.status == BindingStatus.INACTIVE) {
            throw new DomainException("Vínculo de perfil já está inativo");
        }
        this.status = BindingStatus.INACTIVE;
    }

    /**
     * Bloqueia o vínculo
     * 
     * @throws DomainException se o vínculo já estiver bloqueado
     */
    public void block() {
        if (this.status == BindingStatus.BLOCKED) {
            throw new DomainException("Vínculo de perfil já está bloqueado");
        }
        this.status = BindingStatus.BLOCKED;
    }

    /**
     * Desbloqueia o vínculo
     * 
     * @throws DomainException se o vínculo já estiver desbloqueado
     */
    public void unblock() {
        if (this.status != BindingStatus.BLOCKED) {
            throw new DomainException("Vínculo de perfil não está bloqueado");
        }
        this.status = BindingStatus.ACTIVE;
    }

}
