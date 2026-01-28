package com.mssousa.auth.domain.model.system;

import com.mssousa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientSystemTest {

    private SystemId systemId;
    private String clientId;
    private String clientSecret;
    private String name;
    private String redirectUri;

    @BeforeEach
    void setUp() {
        systemId = SystemId.of(1L);
        clientId = "test-client-id";
        clientSecret = "test-secret-123";
        name = "Test System";
        redirectUri = "https://example.com/callback";
    }

    // ==================== Criação e Getters ====================

    @Test
    void testCreateClientSystem() {
        ClientSystem system = new ClientSystem.Builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();

        assertNotNull(system);
        assertEquals(systemId, system.getId());
        assertEquals(clientId, system.getClientId());
        assertEquals(clientSecret, system.getClientSecret());
        assertEquals(name, system.getName());
        assertEquals(redirectUri, system.getRedirectUri());
        assertEquals(SystemStatus.ACTIVE, system.getStatus());
    }

    @Test
    void testCreateClientSystemWithNullStatus() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .build());
        assertEquals(ClientSystem.STATUS_NULL_OR_BLANK, exception.getMessage());
    }

    @Test
    void testGetId() {
        ClientSystem system = ClientSystem.builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();
        assertEquals(systemId, system.getId());
    }

    @Test
    void testGetClientId() {
        ClientSystem system = ClientSystem.builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();
        assertEquals(clientId, system.getClientId());
    }

    @Test
    void testGetClientSecret() {
        ClientSystem system = ClientSystem.builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();
        assertEquals(clientSecret, system.getClientSecret());
    }

    @Test
    void testGetName() {
        ClientSystem system = ClientSystem.builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();
        assertEquals(name, system.getName());
    }

    @Test
    void testGetRedirectUri() {
        ClientSystem system = ClientSystem.builder()
            .id(systemId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .name(name)
            .redirectUri(redirectUri)
            .status(SystemStatus.ACTIVE)
            .build();
        assertEquals(redirectUri, system.getRedirectUri());
    }

    // ==================== Validações no Construtor ====================

    @Test
    void testCreateClientSystemWithNullClientId() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(null)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyClientId() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId("")
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankClientId() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId("   ")
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithNullName() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(null)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyName() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name("")
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankName() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name("   ")
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals(ClientSystem.NAME_NULL_OR_BLANK, exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithNullRedirectUri() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(null)
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyRedirectUri() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri("")
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankRedirectUri() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri("   ")
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithInvalidRedirectUriNoProtocol() {
        DomainException exception = assertThrows(DomainException.class,
            () -> ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri("example.com/callback")
                .status(SystemStatus.ACTIVE)
                .build());
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithValidHttpRedirectUri() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri("http://localhost:3000/callback")
                .status(SystemStatus.ACTIVE)
                .build();
        assertNotNull(system);
        assertEquals("http://localhost:3000/callback", system.getRedirectUri());
    }

    @Test
    void testCreateClientSystemWithValidHttpsRedirectUri() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri("https://example.com/callback")
                .status(SystemStatus.ACTIVE)
                .build();
        assertNotNull(system);
        assertEquals("https://example.com/callback", system.getRedirectUri());
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void testActivateSystem() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.INACTIVE)
                .build();
        
        system.activate();
        
        assertTrue(system.isActive());
        assertEquals(SystemStatus.ACTIVE, system.getStatus());
    }

    @Test
    void testActivateAlreadyActiveSystem() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        system.activate(); // Não deve lançar exceção
        
        assertTrue(system.isActive());
    }

    @Test
    void testDeactivateSystem() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        system.deactivate();
        
        assertTrue(system.isInactive());
        assertEquals(SystemStatus.INACTIVE, system.getStatus());
    }

    @Test
    void testIsActive() {
        ClientSystem activeSystem = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        ClientSystem inactiveSystem = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.INACTIVE)
                .build();
        
        assertTrue(activeSystem.isActive());
        assertFalse(inactiveSystem.isActive());
    }

    @Test
    void testIsInactive() {
        ClientSystem inactiveSystem = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.INACTIVE)
                .build();
        ClientSystem activeSystem = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertTrue(inactiveSystem.isInactive());
        assertFalse(activeSystem.isInactive());
    }

    @Test
    void testStatusTransitions() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        // ACTIVE -> INACTIVE
        system.deactivate();
        assertTrue(system.isInactive());
        
        // INACTIVE -> ACTIVE
        system.activate();
        assertTrue(system.isActive());
    }

    // ==================== Gerenciamento de Configurações ====================

    @Test
    void testUpdateClientSecret() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        String newSecret = "new-secret-456";
        
        system.updateClientSecret(newSecret);
        
        assertEquals(newSecret, system.getClientSecret());
    }

    @Test
    void testUpdateClientSecretWithNull() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateClientSecret(null));
        assertEquals(ClientSystem.CLIENT_SECRET_NULL_OR_BLANK, exception.getMessage());
    }

    @Test
    void testUpdateClientSecretWithEmpty() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateClientSecret(""));
        assertEquals(ClientSystem.CLIENT_SECRET_NULL_OR_BLANK, exception.getMessage());
    }

    @Test
    void testUpdateClientSecretWithBlank() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateClientSecret("   "));
        assertEquals(ClientSystem.CLIENT_SECRET_NULL_OR_BLANK, exception.getMessage());
    }

    @Test
    void testUpdateName() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        String newName = "Updated System Name";
        
        system.updateName(newName);
        
        assertEquals(newName, system.getName());
    }

    @Test
    void testUpdateNameWithNull() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateName(null));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateNameWithEmpty() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateName(""));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateRedirectUri() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        String newRedirectUri = "https://newdomain.com/callback";
        
        system.updateRedirectUri(newRedirectUri);
        
        assertEquals(newRedirectUri, system.getRedirectUri());
    }

    @Test
    void testUpdateRedirectUriWithNull() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateRedirectUri(null));
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    @Test
    void testUpdateRedirectUriWithInvalidProtocol() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        DomainException exception = assertThrows(DomainException.class,
            () -> system.updateRedirectUri("ftp://example.com/callback"));
        assertEquals(ClientSystem.REDIRECT_URI_INVALID, exception.getMessage());
    }

    // ==================== Validação de Acesso ====================

    @Test
    void testCanAcceptAuthenticationWhenActive() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertTrue(system.canAcceptAuthentication());
    }

    @Test
    void testCannotAcceptAuthenticationWhenInactive() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.INACTIVE)
                .build();
        
        assertFalse(system.canAcceptAuthentication());
    }

    @Test
    void testMatchesRedirectUriWithCorrectUri() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertTrue(system.matchesRedirectUri(redirectUri));
    }

    @Test
    void testMatchesRedirectUriWithIncorrectUri() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertFalse(system.matchesRedirectUri("https://different.com/callback"));
    }

    @Test
    void testMatchesRedirectUriWithNull() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertFalse(system.matchesRedirectUri(null));
    }

    @Test
    void testValidateClientSecretWithCorrectSecret() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertTrue(system.verifyClientSecret(clientSecret));
    }

    @Test
    void testValidateClientSecretWithIncorrectSecret() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertFalse(system.verifyClientSecret("wrong-secret"));
    }

    @Test
    void testValidateClientSecretWithNull() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        assertFalse(system.verifyClientSecret(null));
    }

    // ==================== Testes Integrados ====================

    @Test
    void testCompleteSystemLifecycle() {
        // Criar sistema ativo
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        assertTrue(system.canAcceptAuthentication());
        
        // Desativar sistema
        system.deactivate();
        assertFalse(system.canAcceptAuthentication());
        assertTrue(system.isInactive());
        
        // Reativar sistema
        system.activate();
        assertTrue(system.canAcceptAuthentication());
        assertTrue(system.isActive());
    }

    @Test
    void testUpdateConfigurationsFlow() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        // Atualizar nome
        system.updateName("New System Name");
        assertEquals("New System Name", system.getName());
        
        // Atualizar secret
        system.updateClientSecret("new-secret");
        assertEquals("new-secret", system.getClientSecret());
        assertTrue(system.verifyClientSecret("new-secret"));
        assertFalse(system.verifyClientSecret(clientSecret));
        
        // Atualizar redirect URI
        system.updateRedirectUri("https://newsystem.com/callback");
        assertEquals("https://newsystem.com/callback", system.getRedirectUri());
        assertTrue(system.matchesRedirectUri("https://newsystem.com/callback"));
        assertFalse(system.matchesRedirectUri(redirectUri));
    }

    @Test
    void testValidationFlow() {
        ClientSystem system = ClientSystem.builder()
                .id(systemId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .name(name)
                .redirectUri(redirectUri)
                .status(SystemStatus.ACTIVE)
                .build();
        
        // Validar credenciais corretas
        assertTrue(system.verifyClientSecret(clientSecret));
        assertTrue(system.matchesRedirectUri(redirectUri));
        assertTrue(system.canAcceptAuthentication());
        
        // Validar credenciais incorretas
        assertFalse(system.verifyClientSecret("wrong-secret"));
        assertFalse(system.matchesRedirectUri("https://wrong.com/callback"));
        
        // Desativar e verificar que não aceita autenticação
        system.deactivate();
        assertFalse(system.canAcceptAuthentication());
        // Mas ainda valida credenciais
        assertTrue(system.verifyClientSecret(clientSecret));
        assertTrue(system.matchesRedirectUri(redirectUri));
    }
}
