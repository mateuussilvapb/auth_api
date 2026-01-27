package com.mssousa.auth.domain.model.binding.userSystem;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;

/**
 * Entidade de domínio representando um vínculo usuário-sistema.
 * <p>
 * Um vínculo possui identidade única e status, além de possuir por obrigação um usuário e sistema.
 * </p>
 */
public class UserSystem {
    // Mensagens de erro para validações
    private static final String USER_SYSTEM_ID_REQUIRED = "UserSystemId é obrigatório no vínculo UserSystem";
    private static final String USER_ID_REQUIRED = "UserId é obrigatório no vínculo UserSystem";
    private static final String SYSTEM_ID_REQUIRED = "SystemId é obrigatório no vínculo UserSystem";
    private static final String STATUS_REQUIRED = "Status do vínculo não pode ser nulo";
    private static final String INACTIVE_ACCESS = "Usuário não possui acesso ativo ao sistema";

    private final UserSystemId id;
    private final UserId userId;
    private final SystemId systemId;
    private BindingStatus status;

    /**
     * Cria um novo vínculo usuário-sistema.
     *
     * @param builder builder do vínculo
     */
    private UserSystem(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.systemId = builder.systemId;
        this.status = builder.status;

        validate();
    }

    // ==================== Getters ====================
    
    public UserSystemId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public SystemId getSystemId() {
        return systemId;
    }

    public BindingStatus getStatus() {
        return status;
    }

    // ==================== Validações ====================

    private void validate() {
        if (id == null) {
            throw new DomainException(USER_SYSTEM_ID_REQUIRED);
        }
        if (userId == null) {
            throw new DomainException(USER_ID_REQUIRED);
        }
        if (systemId == null) {
            throw new DomainException(SYSTEM_ID_REQUIRED);
        }
        if (status == null) {
            throw new DomainException(STATUS_REQUIRED);
        }
    }

    /**
     * Valida se o vínculo está ativo, lançando uma exceção caso contrário.
     * <p>
     * Se o vínculo não estiver ativo, lança uma exceção.
     * </p>
     */
    public void validateAccess() {
        if (!isActive()) {
            throw new DomainException(INACTIVE_ACCESS);
        }
    }

    // ==================== Gerenciamento de Status ====================
    
    /**
     * Ativa o vínculo, permitindo acesso ao sistema vinculado.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void activate() {
        this.status = BindingStatus.ACTIVE;
    }

    /**
     * Desativa o vínculo, impedindo acesso ao sistema vinculado.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void deactivate() {
        this.status = BindingStatus.INACTIVE;
    }

    /**
     * Bloqueia o vínculo, impedindo acesso ao sistema vinculado.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void block() {
        this.status = BindingStatus.BLOCKED;
    }


    // ==================== Métodos Auxiliares ====================

    /**
     * Verifica se o vínculo está ativo.
     *
     * @return true se o status for ACTIVE
     */
    public boolean isActive() {
        return this.status == BindingStatus.ACTIVE;
    }

    /**
     * Verifica se o vínculo está inativo.
     *
     * @return true se o status for INACTIVE
     */
    public boolean isInactive() {
        return this.status == BindingStatus.INACTIVE;
    }

    /**
     * Verifica se o vínculo está bloqueado.
     *
     * @return true se o status for BLOCKED
     */
    public boolean isBlocked() {
        return this.status == BindingStatus.BLOCKED;
    }

    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UserSystemId id;
        private UserId userId;
        private SystemId systemId;
        private BindingStatus status;

        public Builder id(UserSystemId id) {
            this.id = id;
            return this;
        }

        public Builder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public Builder systemId(SystemId systemId) {
            this.systemId = systemId;
            return this;
        }

        public Builder status(BindingStatus status) {
            this.status = status;
            return this;
        }

        public UserSystem build() {
            // Validações de campos obrigatórios
            if (id == null) {
                throw new DomainException(USER_SYSTEM_ID_REQUIRED);
            }
            if (userId == null) {
                throw new DomainException(USER_ID_REQUIRED);
            }
            if (systemId == null) {
                throw new DomainException(SYSTEM_ID_REQUIRED);
            }
            if (status == null) {
                throw new DomainException(STATUS_REQUIRED);
            }
            return new UserSystem(this);
        }
    }

}
