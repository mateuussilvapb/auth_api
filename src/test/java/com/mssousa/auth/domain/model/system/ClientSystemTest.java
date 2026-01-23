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
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);

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
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, null);

        // Deve usar ACTIVE como padrão
        assertEquals(SystemStatus.ACTIVE, system.getStatus());
    }

    @Test
    void testGetId() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        assertEquals(systemId, system.getId());
    }

    @Test
    void testGetClientId() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        assertEquals(clientId, system.getClientId());
    }

    @Test
    void testGetClientSecret() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        assertEquals(clientSecret, system.getClientSecret());
    }

    @Test
    void testGetName() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        assertEquals(name, system.getName());
    }

    @Test
    void testGetRedirectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        assertEquals(redirectUri, system.getRedirectUri());
    }

    // ==================== Validações no Construtor ====================

    @Test
    void testCreateClientSystemWithNullClientId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, null, clientSecret, name, redirectUri, SystemStatus.ACTIVE));
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyClientId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, "", clientSecret, name, redirectUri, SystemStatus.ACTIVE));
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankClientId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, "   ", clientSecret, name, redirectUri, SystemStatus.ACTIVE));
        assertEquals("Client ID não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithNullName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, null, redirectUri, SystemStatus.ACTIVE));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, "", redirectUri, SystemStatus.ACTIVE));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, "   ", redirectUri, SystemStatus.ACTIVE));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithNullRedirectUri() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, name, null, SystemStatus.ACTIVE));
        assertEquals("Redirect URI não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithEmptyRedirectUri() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, name, "", SystemStatus.ACTIVE));
        assertEquals("Redirect URI não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithBlankRedirectUri() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, name, "   ", SystemStatus.ACTIVE));
        assertEquals("Redirect URI não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithInvalidRedirectUriNoProtocol() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new ClientSystem(systemId, clientId, clientSecret, name, "example.com/callback", SystemStatus.ACTIVE));
        assertEquals("Redirect URI deve começar com http:// ou https://", exception.getMessage());
    }

    @Test
    void testCreateClientSystemWithValidHttpRedirectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, "http://localhost:3000/callback", SystemStatus.ACTIVE);
        assertNotNull(system);
        assertEquals("http://localhost:3000/callback", system.getRedirectUri());
    }

    @Test
    void testCreateClientSystemWithValidHttpsRedirectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, "https://example.com/callback", SystemStatus.ACTIVE);
        assertNotNull(system);
        assertEquals("https://example.com/callback", system.getRedirectUri());
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void testActivateSystem() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.INACTIVE);
        
        system.activate();
        
        assertTrue(system.isActive());
        assertEquals(SystemStatus.ACTIVE, system.getStatus());
    }

    @Test
    void testActivateAlreadyActiveSystem() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        system.activate(); // Não deve lançar exceção
        
        assertTrue(system.isActive());
    }

    @Test
    void testDeactivateSystem() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        system.deactivate();
        
        assertTrue(system.isInactive());
        assertEquals(SystemStatus.INACTIVE, system.getStatus());
    }

    @Test
    void testDeactivateAlreadyInactiveSystem() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.INACTIVE);
        
        DomainException exception = assertThrows(DomainException.class, system::deactivate);
        assertEquals("Sistema já está inativo", exception.getMessage());
    }

    @Test
    void testIsActive() {
        ClientSystem activeSystem = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        ClientSystem inactiveSystem = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.INACTIVE);
        
        assertTrue(activeSystem.isActive());
        assertFalse(inactiveSystem.isActive());
    }

    @Test
    void testIsInactive() {
        ClientSystem inactiveSystem = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.INACTIVE);
        ClientSystem activeSystem = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertTrue(inactiveSystem.isInactive());
        assertFalse(activeSystem.isInactive());
    }

    @Test
    void testStatusTransitions() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
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
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        String newSecret = "new-secret-456";
        
        system.updateClientSecret(newSecret);
        
        assertEquals(newSecret, system.getClientSecret());
    }

    @Test
    void testUpdateClientSecretWithNull() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateClientSecret(null));
        assertEquals("Client secret não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateClientSecretWithEmpty() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateClientSecret(""));
        assertEquals("Client secret não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateClientSecretWithBlank() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateClientSecret("   "));
        assertEquals("Client secret não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateName() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        String newName = "Updated System Name";
        
        system.updateName(newName);
        
        assertEquals(newName, system.getName());
    }

    @Test
    void testUpdateNameWithNull() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateName(null));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateNameWithEmpty() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateName(""));
        assertEquals("Nome do sistema não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateRedirectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        String newRedirectUri = "https://newdomain.com/callback";
        
        system.updateRedirectUri(newRedirectUri);
        
        assertEquals(newRedirectUri, system.getRedirectUri());
    }

    @Test
    void testUpdateRedirectUriWithNull() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateRedirectUri(null));
        assertEquals("Redirect URI não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testUpdateRedirectUriWithInvalidProtocol() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> system.updateRedirectUri("ftp://example.com/callback"));
        assertEquals("Redirect URI deve começar com http:// ou https://", exception.getMessage());
    }

    // ==================== Validação de Acesso ====================

    @Test
    void testCanAcceptAuthenticationWhenActive() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertTrue(system.canAcceptAuthentication());
    }

    @Test
    void testCannotAcceptAuthenticationWhenInactive() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.INACTIVE);
        
        assertFalse(system.canAcceptAuthentication());
    }

    @Test
    void testMatchesRedirectUriWithCorrectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertTrue(system.matchesRedirectUri(redirectUri));
    }

    @Test
    void testMatchesRedirectUriWithIncorrectUri() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertFalse(system.matchesRedirectUri("https://different.com/callback"));
    }

    @Test
    void testMatchesRedirectUriWithNull() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertFalse(system.matchesRedirectUri(null));
    }

    @Test
    void testValidateClientSecretWithCorrectSecret() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertTrue(system.validateClientSecret(clientSecret));
    }

    @Test
    void testValidateClientSecretWithIncorrectSecret() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertFalse(system.validateClientSecret("wrong-secret"));
    }

    @Test
    void testValidateClientSecretWithNull() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        assertFalse(system.validateClientSecret(null));
    }

    // ==================== Testes Integrados ====================

    @Test
    void testCompleteSystemLifecycle() {
        // Criar sistema ativo
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
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
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        // Atualizar nome
        system.updateName("New System Name");
        assertEquals("New System Name", system.getName());
        
        // Atualizar secret
        system.updateClientSecret("new-secret");
        assertEquals("new-secret", system.getClientSecret());
        assertTrue(system.validateClientSecret("new-secret"));
        assertFalse(system.validateClientSecret(clientSecret));
        
        // Atualizar redirect URI
        system.updateRedirectUri("https://newsystem.com/callback");
        assertEquals("https://newsystem.com/callback", system.getRedirectUri());
        assertTrue(system.matchesRedirectUri("https://newsystem.com/callback"));
        assertFalse(system.matchesRedirectUri(redirectUri));
    }

    @Test
    void testValidationFlow() {
        ClientSystem system = new ClientSystem(systemId, clientId, clientSecret, name, redirectUri, SystemStatus.ACTIVE);
        
        // Validar credenciais corretas
        assertTrue(system.validateClientSecret(clientSecret));
        assertTrue(system.matchesRedirectUri(redirectUri));
        assertTrue(system.canAcceptAuthentication());
        
        // Validar credenciais incorretas
        assertFalse(system.validateClientSecret("wrong-secret"));
        assertFalse(system.matchesRedirectUri("https://wrong.com/callback"));
        
        // Desativar e verificar que não aceita autenticação
        system.deactivate();
        assertFalse(system.canAcceptAuthentication());
        // Mas ainda valida credenciais
        assertTrue(system.validateClientSecret(clientSecret));
        assertTrue(system.matchesRedirectUri(redirectUri));
    }
}
