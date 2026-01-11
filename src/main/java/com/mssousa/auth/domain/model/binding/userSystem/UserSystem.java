package com.mssousa.auth.domain.model.binding.userSystem;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.user.User;

/**
 * Entidade de domínio representando um vínculo usuário-sistema.
 * <p>
 * Um vínculo possui identidade única e status, além de possuir por obrigação um usuário e sistema.
 * </p>
 */
public class UserSystem {
    private final UserSystemId id;
    private final User user;
    private final ClientSystem system;
    private BindingStatus status;

    /**
     * Cria um novo vínculo usuário-sistema.
     *
     * @param id identificador único do vínculo
     * @param user usuário associado ao vínculo
     * @param system sistema associado ao vínculo
     * @param status status do vínculo
     */
    public UserSystem(UserSystemId id, User user, ClientSystem system, BindingStatus status) {
        validateId(id);
        validateUser(user);
        validateSystem(system);
        validateStatus(status);

        this.id = id;
        this.user = user;
        this.system = system;
        this.status = status;
    }

    // ==================== Getters ====================
    
    public UserSystemId getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ClientSystem getSystem() {
        return system;
    }

    public BindingStatus getStatus() {
        return status;
    }

    // ==================== Validações ====================

    private void validateId(UserSystemId id) {
        if (id == null) {
            throw new DomainException("UserSystemId é obrigatório no vínculo UserSystem");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new DomainException("User é obrigatório no vínculo UserSystem");
        }
    }

    private void validateSystem(ClientSystem system) {
        if (system == null) {
            throw new DomainException("System é obrigatório no vínculo UserSystem");
        }
    }

    private void validateStatus(BindingStatus status) {
        if (status == null) {
            throw new DomainException("Status do vínculo não pode ser nulo");
        }
    }

    /**
     * Valida se o vínculo está ativo, lançando uma exceção caso contrário.
     * <p>
     * Se o vínculo não estiver ativo, lança uma exceção.
     * </p>
     */
    public void validateAccess() {
        if (!user.isActive()) {
            throw new DomainException("Usuário não está ativo");
        }
        if (!system.isActive()) {
            throw new DomainException("Sistema não está ativo");
        }
        if (!isActive()) {
            throw new DomainException("Usuário não possui acesso ativo ao sistema");
        }
    }

    // ==================== Gerenciamento de Status ====================
    
    /**
     * Ativa o vínculo, permitindo acesso ao sistema vinculado.
     * <p>
     * Se o vínculo já estiver ativo, lança uma exceção.
     * </p>
     */
    public void activate() {
        if (this.status == BindingStatus.ACTIVE) {
            throw new DomainException("Vínculo já está ativo");
        }
        this.status = BindingStatus.ACTIVE;
    }

    /**
     * Desativa o vínculo, impedindo acesso ao sistema vinculado.
     * <p>
     * Se o vínculo já estiver inativo, lança uma exceção.
     * </p>
     */
    public void deactivate() {
        if (this.status == BindingStatus.INACTIVE) {
            throw new DomainException("Vínculo já está inativo");
        }
        this.status = BindingStatus.INACTIVE;
    }

    /**
     * Bloqueia o vínculo, impedindo acesso ao sistema vinculado.
     * <p>
     * Se o vínculo já estiver bloqueado, lança uma exceção.
     * </p>
     */
    public void block() {
        if (this.status == BindingStatus.BLOCKED) {
            throw new DomainException("Vínculo já está bloqueado");
        }
        this.status = BindingStatus.BLOCKED;
    }

    /**
     * Desbloqueia o vínculo, retornando-o para o status ATIVO.
     * <p>
     * Se o vínculo não estiver bloqueado, lança uma exceção.
     * </p>
     */
    public void unblock() {
        if (this.status != BindingStatus.BLOCKED) {
            throw new DomainException("Vínculo não está bloqueado");
        }
        this.status = BindingStatus.ACTIVE;
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

}
