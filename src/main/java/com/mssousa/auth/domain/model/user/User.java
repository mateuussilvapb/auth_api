package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;

/**
 * Entidade de domínio representando um Usuário no sistema de autenticação.
 * <p>
 * Um usuário possui identidade única, credenciais, status e pode ter privilégios master.
 * Usuários master têm acesso irrestrito a todos os sistemas clientes.
 * </p>
 */
public class User {
    
    // Mensagens de erro para validações
    public static final String DEFAULT_ERROR_ID = "Id não pode ser nulo";
    public static final String DEFAULT_ERROR_NAME = "Nome do usuário não pode ser nulo ou vazio";
    public static final String ERROR_USER_NOT_BLOCKED = "Usuário não está bloqueado";

    private final UserId id;
    private final Username username;
    private Email email;
    private Password password;
    private String name;
    private boolean master;
    private UserStatus status;

    /**
     * Cria um novo usuário.
     * 
     * @param builder Builder com os dados do usuário
     */
    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.name = builder.name;
        this.master = builder.master;
        this.status = builder.status;
        
        validate();
    }

    // ==================== Validações ====================

    private void validate() {
        if (id == null) {
            throw new DomainException(DEFAULT_ERROR_ID);
        }
        if (username == null) {
            throw new DomainException(Username.DEFAULT_ERROR_USERNAME);
        }
        if (email == null) {
            throw new DomainException(Email.DEFAULT_ERROR_EMAIL);
        }
        if (password == null) {
            throw new DomainException(Password.DEFAULT_ERROR_PASSWORD);
        }
        if (name == null || name.isBlank()) {
            throw new DomainException(DEFAULT_ERROR_NAME);
        }
        // Status é garantido pelo Builder (sempre != null)
    }

    // ==================== Getters ====================

    public UserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public boolean isMaster() {
        return master;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Ativa o usuário, permitindo acesso ao sistema.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Bloqueia o usuário, impedindo acesso ao sistema.
     * Usado geralmente para violações de segurança ou políticas.
     * Operação idempotente.
     */
    public void block() {
        this.status = UserStatus.BLOCKED;
    }

    /**
     * Desabilita o usuário temporariamente.
     * Diferente de bloqueio, usado para suspensões temporárias.
     * Operação idempotente.
     */
    public void disable() {
        this.status = UserStatus.DISABLED;
    }

    /**
     * Verifica se o usuário está ativo.
     *
     * @return true se o status for ACTIVE
     */
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    /**
     * Verifica se o usuário está bloqueado.
     *
     * @return true se o status for BLOCKED
     */
    public boolean isBlocked() {
        return this.status == UserStatus.BLOCKED;
    }

    /**
     * Verifica se o usuário está desabilitado.
     *
     * @return true se o status for DISABLED
     */
    public boolean isDisabled() {
        return this.status == UserStatus.DISABLED;
    }

    // ==================== Gerenciamento de Senha ====================

    /**
     * Atualiza a senha do usuário.
     *
     * @param newPassword nova senha (já em formato hash)
     * @throws DomainException se a nova senha for nula
     */
    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new DomainException(Password.DEFAULT_ERROR_PASSWORD);
        }
        this.password = newPassword;
    }

    /**
     * Verifica se a senha em texto plano corresponde à senha armazenada.
     *
     * @param plainPassword senha em texto plano para verificar
     * @return true se a senha corresponder
     */
    public boolean verifyPassword(String plainPassword) {
        return this.password.matches(plainPassword);
    }

    // ==================== Gerenciamento de Perfil Master ====================

    /**
     * Promove o usuário para master, concedendo acesso total.
     */
    public void promoteToMaster() {
        this.master = true;
    }

    /**
     * Remove privilégios master do usuário.
     */
    public void demoteFromMaster() {
        this.master = false;
    }

    // ==================== Controle de Acesso ====================

    /**
     * Verifica se o usuário pode fazer login no sistema.
     * <p>
     * Esta verificação é sobre o estado do USUÁRIO, não sobre sistemas externos.
     * Usuários master sempre podem fazer login.
     * Usuários não-master precisam estar ativos.
     * </p>
     * <p>
     * Nota: A validação de acesso a sistemas específicos deve ser feita
     * na camada de Application, pois envolve múltiplos agregados.
     * </p>
     *
     * @return true se o usuário pode fazer login
     */
    public boolean canLogin() {
        return master || status == UserStatus.ACTIVE;
    }

    // ==================== Atualização de Perfil ====================

    /**
     * Atualiza o nome do usuário.
     *
     * @param name novo nome
     * @throws DomainException se o nome for nulo ou vazio
     */
    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(DEFAULT_ERROR_NAME);
        }
        this.name = name;
    }

    /**
     * Atualiza o email do usuário.
     *
     * @param email novo email
     * @throws DomainException se o email for nulo
     */
    public void updateEmail(Email email) {
        if (email == null) {
            throw new DomainException(Email.DEFAULT_ERROR_EMAIL);
        }
        this.email = email;
    }

    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UserId id;
        private Username username;
        private Email email;
        private Password password;
        private String name;
        private boolean master = false;
        private UserStatus status = UserStatus.ACTIVE;

        public Builder id(UserId id) {
            this.id = id;
            return this;
        }

        public Builder username(Username username) {
            this.username = username;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder password(Password password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder master(boolean master) {
            this.master = master;
            return this;
        }

        public Builder status(UserStatus status) {
            if (status == null) {
                this.status = UserStatus.ACTIVE;
                return this;
            }
            this.status = status;
            return this;
        }

        public User build() {
            // Validação de campos obrigatórios
            if (id == null) {
                throw new DomainException(User.DEFAULT_ERROR_ID);
            }
            if (username == null) {
                throw new DomainException(Username.DEFAULT_ERROR_USERNAME);
            }
            if (email == null) {
                throw new DomainException(Email.DEFAULT_ERROR_EMAIL);
            }
            if (password == null) {
                throw new DomainException(Password.DEFAULT_ERROR_PASSWORD);
            }
            if (name == null || name.isBlank()) {
                throw new DomainException(User.DEFAULT_ERROR_NAME);
            }
            
            return new User(this);
        }
    }
}