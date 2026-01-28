package com.mssousa.auth.domain.model.system;

import com.mssousa.auth.domain.exception.DomainException;

/**
 * Entidade de domínio representando um Sistema Cliente no Auth Server.
 * <p>
 * Um sistema cliente é uma aplicação que consome autenticação do Auth Server.
 * Cada sistema possui identificadores OAuth2, URI de redirecionamento e status.
 * </p>
 */
public class ClientSystem {
    // Mensagens de erro para validações
    public static final String ID_NULL_OR_BLANK = "ID do sistema não pode ser nulo ou vazio";
    public static final String CLIENT_ID_NULL_OR_BLANK = "Client ID não pode ser nulo ou vazio";
    public static final String CLIENT_SECRET_NULL_OR_BLANK = "Client Secret não pode ser nulo ou vazio";
    public static final String NAME_NULL_OR_BLANK = "Nome do sistema não pode ser nulo ou vazio";
    public static final String REDIRECT_URI_INVALID = "Redirect URI não pode ser nulo ou vazio e deve começar com http:// ou https://";
    public static final String STATUS_NULL_OR_BLANK = "Status do sistema não pode ser nulo ou vazio";

    private final SystemId id;
    private final String clientId;
    private String clientSecret;
    private String name;
    private String redirectUri;
    private SystemStatus status;

    /**
     * Cria um novo sistema cliente.
     *
     * @param builder builder do sistema cliente
     */
    private ClientSystem(Builder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.name = builder.name;
        this.redirectUri = builder.redirectUri;
        this.status = builder.status != null ? builder.status : SystemStatus.ACTIVE;

        validate();
    }

    // ==================== Validações ====================

    private void validate() {
        if (id == null) {
            throw new DomainException(ID_NULL_OR_BLANK);
        }

        if (clientId == null || clientId.isBlank()) {
            throw new DomainException(CLIENT_ID_NULL_OR_BLANK);
        }

        validateClientSecret(clientSecret);

        validateName(name);

        validateRedirectUri(redirectUri);

        if (status == null) {
            throw new DomainException(STATUS_NULL_OR_BLANK);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException(NAME_NULL_OR_BLANK);
        }
    }

    private void validateRedirectUri(String redirectUri) {
        if (redirectUri == null || redirectUri.isBlank() || !redirectUri.startsWith("http://") && !redirectUri.startsWith("https://")) {
            throw new DomainException(REDIRECT_URI_INVALID);
        }
    }

    public void validateClientSecret(String clientSecret) {
        if (clientSecret == null || clientSecret.isBlank()) {
            throw new DomainException(CLIENT_SECRET_NULL_OR_BLANK);
        }
    }

    // ==================== Getters ====================

    public SystemId getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getName() {
        return name;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public SystemStatus getStatus() {
        return status;
    }

    // ==================== Gerenciamento de Status ====================

    /**
     * Ativa o sistema, permitindo autenticações.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void activate() {
        this.status = SystemStatus.ACTIVE;
    }

    /**
     * Desativa o sistema, impedindo novas autenticações.
     * Operação idempotente - pode ser chamada múltiplas vezes sem efeitos colaterais.
     */
    public void deactivate() {
        this.status = SystemStatus.INACTIVE;
    }

    /**
     * Verifica se o sistema está ativo.
     *
     * @return true se o status for ACTIVE
     */
    public boolean isActive() {
        return this.status == SystemStatus.ACTIVE;
    }

    /**
     * Verifica se o sistema está inativo.
     *
     * @return true se o status for INACTIVE
     */
    public boolean isInactive() {
        return !isActive();
    }

    // ==================== Gerenciamento de Configurações ====================

    /**
     * Atualiza o client secret do sistema.
     *
     * @param newClientSecret novo client secret
     * @throws DomainException se o novo secret for nulo ou vazio
     */
    public void updateClientSecret(String newClientSecret) {
        validateClientSecret(newClientSecret);
        this.clientSecret = newClientSecret;
    }

    /**
     * Atualiza o nome do sistema.
     *
     * @param newName novo nome
     * @throws DomainException se o novo nome for nulo ou vazio
     */
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    /**
     * Atualiza a URI de redirecionamento do sistema.
     *
     * @param newRedirectUri nova URI de redirecionamento
     * @throws DomainException se a nova URI for inválida
     */
    public void updateRedirectUri(String newRedirectUri) {
        validateRedirectUri(newRedirectUri);
        this.redirectUri = newRedirectUri;
    }

    // ==================== Validação de Acesso ====================

    /**
     * Verifica se o sistema pode aceitar autenticações.
     *
     * @return true se o sistema estiver ativo
     */
    public boolean canAcceptAuthentication() {
        return this.status == SystemStatus.ACTIVE;
    }

    /**
     * Verifica se a URI de redirecionamento fornecida corresponde à registrada.
     *
     * @param providedRedirectUri URI fornecida na requisição
     * @return true se a URI corresponder
     */
    public boolean matchesRedirectUri(String providedRedirectUri) {
        if (providedRedirectUri == null) {
            return false;
        }
        return this.redirectUri.equals(providedRedirectUri);
    }

    /**
     * Valida se o client secret fornecido corresponde ao registrado.
     *
     * @param providedSecret secret fornecido na requisição
     * @return true se o secret corresponder
     */
    public boolean verifyClientSecret(String providedSecret) {
        if (providedSecret == null) {
            return false;
        }
        return this.clientSecret.equals(providedSecret);
    }

    // ==================== Padrão Builder ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SystemId id;
        private String clientId;
        private String clientSecret;
        private String name;
        private String redirectUri;
        private SystemStatus status;

        public Builder id(SystemId id) {
            this.id = id;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder status(SystemStatus status) {
            this.status = status;
            return this;
        }

        public ClientSystem build() {

            if (id == null) {
                throw new DomainException(ID_NULL_OR_BLANK);
            }

            if (clientId == null || clientId.isBlank()) {
                throw new DomainException(CLIENT_ID_NULL_OR_BLANK);
            }

            if (clientSecret == null || clientSecret.isBlank()) {
                throw new DomainException(CLIENT_SECRET_NULL_OR_BLANK);
            }

            if (name == null || name.isBlank()) {
                throw new DomainException(NAME_NULL_OR_BLANK);
            }

            if (redirectUri == null || redirectUri.isBlank() || !redirectUri.startsWith("http://") && !redirectUri.startsWith("https://")) {
                throw new DomainException(REDIRECT_URI_INVALID);
            }

            if (status == null) {
                throw new DomainException(STATUS_NULL_OR_BLANK);
            }


            return new ClientSystem(this);
        }
    }
}
