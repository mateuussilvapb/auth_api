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

    private final SystemId id;
    private final String clientId;
    private String clientSecret;
    private String name;
    private String redirectUri;
    private SystemStatus status;

    /**
     * Cria um novo sistema cliente.
     *
     * @param id identificador único do sistema
     * @param clientId identificador público OAuth2
     * @param clientSecret segredo do cliente (apenas server-to-server)
     * @param name nome do sistema
     * @param redirectUri URI de redirecionamento validada no authorize
     * @param status status do sistema
     */
    public ClientSystem(SystemId id, String clientId, String clientSecret, String name, String redirectUri, SystemStatus status) {
        validateClientId(clientId);
        validateName(name);
        validateRedirectUri(redirectUri);
        
        this.id = id;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.name = name;
        this.redirectUri = redirectUri;
        this.status = status != null ? status : SystemStatus.ACTIVE;
    }

    // ==================== Validações ====================

    private void validateClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID não pode ser nulo ou vazio");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do sistema não pode ser nulo ou vazio");
        }
    }

    private void validateRedirectUri(String redirectUri) {
        if (redirectUri == null || redirectUri.isBlank()) {
            throw new IllegalArgumentException("Redirect URI não pode ser nulo ou vazio");
        }
        // Validação básica de URI
        if (!redirectUri.startsWith("http://") && !redirectUri.startsWith("https://")) {
            throw new IllegalArgumentException("Redirect URI deve começar com http:// ou https://");
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
     */
    public void activate() {
        this.status = SystemStatus.ACTIVE;
    }

    /**
     * Desativa o sistema, impedindo novas autenticações.
     *
     * @throws DomainException se o sistema já estiver inativo
     */
    public void deactivate() {
        if (this.status == SystemStatus.INACTIVE) {
            throw new DomainException("Sistema já está inativo");
        }
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
        return this.status == SystemStatus.INACTIVE;
    }

    // ==================== Gerenciamento de Configurações ====================

    /**
     * Atualiza o client secret do sistema.
     *
     * @param newClientSecret novo client secret
     * @throws IllegalArgumentException se o novo secret for nulo ou vazio
     */
    public void updateClientSecret(String newClientSecret) {
        if (newClientSecret == null || newClientSecret.isBlank()) {
            throw new IllegalArgumentException("Client secret não pode ser nulo ou vazio");
        }
        this.clientSecret = newClientSecret;
    }

    /**
     * Atualiza o nome do sistema.
     *
     * @param newName novo nome
     * @throws IllegalArgumentException se o novo nome for nulo ou vazio
     */
    public void updateName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    /**
     * Atualiza a URI de redirecionamento do sistema.
     *
     * @param newRedirectUri nova URI de redirecionamento
     * @throws IllegalArgumentException se a nova URI for inválida
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
    public boolean validateClientSecret(String providedSecret) {
        if (providedSecret == null) {
            return false;
        }
        return this.clientSecret.equals(providedSecret);
    }
}

