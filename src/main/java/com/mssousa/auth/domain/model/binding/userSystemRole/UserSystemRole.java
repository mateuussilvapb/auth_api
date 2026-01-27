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
    // Mensagens de erro para validações
    private static final String USER_SYSTEM_ROLE_ID_NULL = "UserSystemRoleId é obrigatório no vínculo UserSystemRole";
    private static final String USER_SYSTEM_ID_NULL = "UserSystemId é obrigatório no vínculo UserSystemRole";
    private static final String SYSTEM_ROLE_ID_NULL = "SystemRoleId é obrigatório no vínculo UserSystemRole";
    private static final String STATUS_NULL = "Status do vínculo UserSystemRole não pode ser nulo";
    private static final String INACTIVE_STATUS = "Perfil não está ativo para este usuário no sistema";

    private final UserSystemRoleId id;
    private final UserSystemId userSystemId;
    private final SystemRoleId systemRoleId;

    private BindingStatus status;

    /**
     * Cria um novo vínculo usuário-perfil.
     * 
     * @param builder builder do vínculo usuário-perfil
     */
    private UserSystemRole(Builder builder) {
        this.id = builder.id;
        this.userSystemId = builder.userSystemId;
        this.systemRoleId = builder.systemRoleId;
        this.status = builder.status;

        validate();
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

    private void validate() {
        if (id == null) {
            throw new DomainException(USER_SYSTEM_ROLE_ID_NULL);
        }

        if (userSystemId == null) {
            throw new DomainException(USER_SYSTEM_ID_NULL);
        }

        if (systemRoleId == null) {
            throw new DomainException(SYSTEM_ROLE_ID_NULL);
        }

        if (status == null) {
            throw new DomainException(STATUS_NULL);
        }
    }

    
    /**
     * Valida se o vínculo está ativo
     * 
     * @throws DomainException se o vínculo não estiver ativo
     */
    public void validateAccess() {
        if (!isActive()) {
            throw new DomainException(INACTIVE_STATUS);
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
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void activate() {
        this.status = BindingStatus.ACTIVE;
    }

    /**
     * Desativa o vínculo
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void deactivate() {
        this.status = BindingStatus.INACTIVE;
    }

    /**
     * Bloqueia o vínculo
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void block() {
        this.status = BindingStatus.BLOCKED;
    }

    /**
     * Desbloqueia o vínculo
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void unblock() {
        this.status = BindingStatus.ACTIVE;
    }

    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UserSystemRoleId id;
        private UserSystemId userSystemId;
        private SystemRoleId systemRoleId;
        private BindingStatus status;

        public Builder id(UserSystemRoleId id) {
            this.id = id;
            return this;
        }

        public Builder userSystemId(UserSystemId userSystemId) {
            this.userSystemId = userSystemId;
            return this;
        }

        public Builder systemRoleId(SystemRoleId systemRoleId) {
            this.systemRoleId = systemRoleId;
            return this;
        }

        public Builder status(BindingStatus status) {
            this.status = status;
            return this;
        }

        public UserSystemRole build() {
            if (this.id == null) {
                throw new DomainException(USER_SYSTEM_ROLE_ID_NULL);
            }
            if (this.userSystemId == null) {
                throw new DomainException(USER_SYSTEM_ID_NULL);
            }
            if (this.systemRoleId == null) {
                throw new DomainException(SYSTEM_ROLE_ID_NULL);
            }
            if (this.status == null) {
                throw new DomainException(STATUS_NULL);
            }
            return new UserSystemRole(this);
        }
    }

}
