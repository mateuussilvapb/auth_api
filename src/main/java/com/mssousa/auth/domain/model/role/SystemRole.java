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
    // Mensagens de erro para validações
    private static final String ID_REQUIRED = "ID do perfil não pode ser nulo";
    private static final String SYSTEM_ID_REQUIRED = "SystemId é obrigatório no vínculo UserSystem";
    private static final String CODE_REQUIRED = "Código do perfil não pode ser nulo ou vazio";
    private static final String STATUS_REQUIRED = "Status do vínculo não pode ser nulo";

    private final SystemRoleId id;
    private final SystemId system_id;
    private final String code;
    private String description;
    private SystemRoleStatus status;

    /**
     * Cria um novo perfil.
     *
     * @param builder builder do perfil
     */
    private SystemRole(Builder builder) {
        this.id = builder.id;
        this.system_id = builder.system_id;
        this.code = builder.code;
        this.description = builder.description;
        this.status = builder.status;

        validate();
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

    private void validate() {
        if (id == null) {
            throw new DomainException(ID_REQUIRED);
        }

        if (system_id == null) {
            throw new DomainException(SYSTEM_ID_REQUIRED);
        }

        if (code == null || code.isBlank()) {
            throw new DomainException(CODE_REQUIRED);
        }

        if (status == null) {
            throw new DomainException(STATUS_REQUIRED);
        }
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Ativa o perfil, permitindo que ele seja atribuído a usuários.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void activate() {
        this.status = SystemRoleStatus.ACTIVE;
    }

    /**
     * Desativa o perfil, impedindo que ele seja atribuído a usuários.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void deactivate() {
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
        return !this.isActive();
    }

    // ==================== Gerenciamento de Configurações ====================

    /**
     * Atualiza a descrição do perfil.
     *
     * @param newDescription nova descrição
     */
    public void updateDescription(String newDescription) {
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

    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SystemRoleId id;
        private SystemId system_id;
        private String code;
        private String description;
        private SystemRoleStatus status;

        public Builder id(SystemRoleId id) {
            this.id = id;
            return this;
        }

        public Builder system_id(SystemId system_id) {
            this.system_id = system_id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder description(String description) {
            this.description = description != null ? description : "";
            return this;
        }

        public Builder status(SystemRoleStatus status) {
            this.status = status;
            return this;
        }

        public SystemRole build() {
            if (this.id == null) {
                throw new DomainException(ID_REQUIRED);
            }
            if (this.system_id == null) {
                throw new DomainException(SYSTEM_ID_REQUIRED);
            }
            if (this.code == null || this.code.isBlank()) {
                throw new DomainException(CODE_REQUIRED);
            }
            if (this.status == null) {
                throw new DomainException(STATUS_REQUIRED);
            }
            return new SystemRole(this);
        }
    }
}
