package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.ClientSystem;

/**
 * Entidade de domínio representando um Usuário no sistema de autenticação.
 * <p>
 * Um usuário possui identidade única, credenciais, status e pode ter privilégios master.
 * Usuários master têm acesso irrestrito a todos os sistemas clientes.
 * </p>
 */
public class User {

    private final UserId id;
    private final Username username;
    private final Email email;
    private Password password;
    private boolean master;
    private UserStatus status;
    private String name;

    /**
     * Cria um novo usuário.
     *
     * @param id identificador único do usuário
     * @param username nome de usuário
     * @param email endereço de e-mail
     * @param password senha (hash BCrypt)
     * @param master indica se o usuário é master
     * @param status status do usuário
     * @param name nome do usuário
     */
    public User(UserId id, Username username, Email email, Password password, boolean master, UserStatus status, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.master = master;
        this.status = status;
        this.name = name;
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
     * Se o usuário já estiver ativo, nenhuma ação é tomada.
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Bloqueia o usuário, impedindo acesso ao sistema.
     * Usado geralmente para violações de segurança ou políticas.
     *
     * @throws DomainException se o usuário já estiver bloqueado
     */
    public void block() {
        if (this.status == UserStatus.BLOCKED) {
            throw new DomainException("Usuário já está bloqueado");
        }
        this.status = UserStatus.BLOCKED;
    }

    /**
     * Desbloqueia o usuário, restaurando status ativo.
     *
     * @throws DomainException se o usuário não estiver bloqueado
     */
    public void unblock() {
        if (this.status != UserStatus.BLOCKED) {
            throw new DomainException("Usuário não está bloqueado");
        }
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Desabilita o usuário temporariamente.
     * Diferente de bloqueio, usado para suspensões temporárias.
     *
     * @throws DomainException se o usuário já estiver desabilitado
     */
    public void disable() {
        if (this.status == UserStatus.DISABLED) {
            throw new DomainException("Usuário já está desabilitado");
        }
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
     * @throws IllegalArgumentException se a nova senha for nula
     */
    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("Nova senha não pode ser nula");
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
     * Verifica se o usuário pode acessar um sistema cliente específico.
     * <p>
     * Usuários master sempre podem acessar.
     * Usuários não-master precisam estar ativos.
     * </p>
     *
     * @param system sistema cliente a verificar acesso
     * @return true se o usuário pode acessar o sistema
     */
    public boolean canAccess(ClientSystem system) {
        if (master) {
            return true;
        }
        return status == UserStatus.ACTIVE;
    }

    /**
     * Verifica se o usuário pode fazer login no sistema.
     * <p>
     * Usuários master sempre podem fazer login.
     * Usuários não-master precisam estar ativos.
     * </p>
     *
     * @return true se o usuário pode fazer login
     */
    public boolean canLogin() {
        if (master) {
            return true;
        }
        return status == UserStatus.ACTIVE;
    }
}
