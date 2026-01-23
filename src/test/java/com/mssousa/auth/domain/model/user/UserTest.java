package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.system.SystemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private UserId userId;
    private Username username;
    private Email email;
    private Password password;
    private String name;

    @BeforeEach
    void setUp() {
        userId = UserId.of(1L);
        username = new Username("testuser");
        email = new Email("test@example.com");
        password = Password.fromPlainText("password123");
        name = "Test User";
    }

    // ==================== Criação e Getters ====================

    @Test
    void testCreateUser() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertFalse(user.isMaster());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(name, user.getName());
    }

    @Test
    void testCreateMasterUser() {
        User user = new User(userId, username, email, password, true, UserStatus.ACTIVE, name);

        assertTrue(user.isMaster());
    }

    @Test
    void testGetId() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertEquals(userId, user.getId());
    }

    @Test
    void testGetUsername() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertEquals(username, user.getUsername());
    }

    @Test
    void testGetEmail() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertEquals(email, user.getEmail());
    }


    @Test
    void testGetName() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertEquals(name, user.getName());
    }

    // ==================== Atualização de Perfil ====================

    @Test
    void testUpdateProfile() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        String newName = "Updated Name";
        String newEmail = "updated@example.com";

        user.updateProfile(newName, newEmail);

        assertEquals(newName, user.getName());
        assertEquals(new Email(newEmail), user.getEmail());
    }

    @Test
    void testUpdateProfileWithNulls() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);

        user.updateProfile(null, null);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @Test
    void testUpdateProfileWithEmptyStrings() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);

        user.updateProfile("", "  ");

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void testActivateUser() {
        User user = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        
        user.activate();
        
        assertTrue(user.isActive());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void testActivateAlreadyActiveUser() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        user.activate(); // Não deve lançar exceção
        
        assertTrue(user.isActive());
    }

    @Test
    void testBlockUser() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        user.block();
        
        assertTrue(user.isBlocked());
        assertEquals(UserStatus.BLOCKED, user.getStatus());
    }

    @Test
    void testBlockAlreadyBlockedUser() {
        User user = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        
        DomainException exception = assertThrows(DomainException.class, user::block);
        assertEquals("Usuário já está bloqueado", exception.getMessage());
    }

    @Test
    void testUnblockUser() {
        User user = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        
        user.unblock();
        
        assertTrue(user.isActive());
        assertFalse(user.isBlocked());
    }

    @Test
    void testUnblockNotBlockedUser() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        DomainException exception = assertThrows(DomainException.class, user::unblock);
        assertEquals("Usuário não está bloqueado", exception.getMessage());
    }

    @Test
    void testDisableUser() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        user.disable();
        
        assertTrue(user.isDisabled());
        assertEquals(UserStatus.DISABLED, user.getStatus());
    }

    @Test
    void testDisableAlreadyDisabledUser() {
        User user = new User(userId, username, email, password, false, UserStatus.DISABLED, name);
        
        DomainException exception = assertThrows(DomainException.class, user::disable);
        assertEquals("Usuário já está desabilitado", exception.getMessage());
    }

    @Test
    void testIsActive() {
        User activeUser = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        User blockedUser = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        
        assertTrue(activeUser.isActive());
        assertFalse(blockedUser.isActive());
    }

    @Test
    void testIsBlocked() {
        User blockedUser = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        User activeUser = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertTrue(blockedUser.isBlocked());
        assertFalse(activeUser.isBlocked());
    }

    @Test
    void testIsDisabled() {
        User disabledUser = new User(userId, username, email, password, false, UserStatus.DISABLED, name);
        User activeUser = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertTrue(disabledUser.isDisabled());
        assertFalse(activeUser.isDisabled());
    }

    @Test
    void testStatusTransitions() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        // ACTIVE -> BLOCKED
        user.block();
        assertTrue(user.isBlocked());
        
        // BLOCKED -> ACTIVE
        user.unblock();
        assertTrue(user.isActive());
        
        // ACTIVE -> DISABLED
        user.disable();
        assertTrue(user.isDisabled());
        
        // DISABLED -> ACTIVE
        user.activate();
        assertTrue(user.isActive());
    }

    // ==================== Gerenciamento de Senha ====================

    @Test
    void testChangePassword() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        Password newPassword = Password.fromPlainText("newPassword456");
        
        user.changePassword(newPassword);
        
        assertEquals(newPassword, user.getPassword());
        assertTrue(user.verifyPassword("newPassword456"));
    }

    @Test
    void testChangePasswordWithNull() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> user.changePassword(null));
        assertEquals("Nova senha não pode ser nula", exception.getMessage());
    }

    @Test
    void testVerifyCorrectPassword() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertTrue(user.verifyPassword("password123"));
    }

    @Test
    void testVerifyIncorrectPassword() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertFalse(user.verifyPassword("wrongPassword"));
    }

    @Test
    void testVerifyPasswordWithNull() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertFalse(user.verifyPassword(null));
    }

    // ==================== Gerenciamento Master ====================

    @Test
    void testPromoteToMaster() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertFalse(user.isMaster());
        
        user.promoteToMaster();
        
        assertTrue(user.isMaster());
    }

    @Test
    void testDemoteFromMaster() {
        User user = new User(userId, username, email, password, true, UserStatus.ACTIVE, name);
        assertTrue(user.isMaster());
        
        user.demoteFromMaster();
        
        assertFalse(user.isMaster());
    }

    @Test
    void testMasterCanAccessAnything() {
        User master = new User(userId, username, email, password, true, UserStatus.BLOCKED, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertTrue(master.canAccess(system));
    }

    @Test
    void testNonMasterRequiresActiveStatus() {
        User blockedUser = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        User activeUser = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertFalse(blockedUser.canAccess(system));
        assertTrue(activeUser.canAccess(system));
    }

    // ==================== Controle de Acesso ====================

    @Test
    void testCanAccessWhenActive() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertTrue(user.canAccess(system));
    }

    @Test
    void testCannotAccessWhenBlocked() {
        User user = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertFalse(user.canAccess(system));
    }

    @Test
    void testCannotAccessWhenDisabled() {
        User user = new User(userId, username, email, password, false, UserStatus.DISABLED, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertFalse(user.canAccess(system));
    }

    @Test
    void testMasterCanAlwaysAccess() {
        User masterBlocked = new User(userId, username, email, password, true, UserStatus.BLOCKED, name);
        User masterDisabled = new User(userId, username, email, password, true, UserStatus.DISABLED, name);
        User masterActive = new User(userId, username, email, password, true, UserStatus.ACTIVE, name);
        ClientSystem system = new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE);
        
        assertTrue(masterBlocked.canAccess(system));
        assertTrue(masterDisabled.canAccess(system));
        assertTrue(masterActive.canAccess(system));
    }

    // ==================== Login ====================

    @Test
    void testCanLoginWhenActive() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        assertTrue(user.canLogin());
    }

    @Test
    void testCannotLoginWhenBlocked() {
        User user = new User(userId, username, email, password, false, UserStatus.BLOCKED, name);
        
        assertFalse(user.canLogin());
    }

    @Test
    void testCannotLoginWhenDisabled() {
        User user = new User(userId, username, email, password, false, UserStatus.DISABLED, name);
        
        assertFalse(user.canLogin());
    }

    @Test
    void testMasterCanAlwaysLogin() {
        User masterBlocked = new User(userId, username, email, password, true, UserStatus.BLOCKED, name);
        User masterDisabled = new User(userId, username, email, password, true, UserStatus.DISABLED, name);
        
        assertTrue(masterBlocked.canLogin());
        assertTrue(masterDisabled.canLogin());
    }

    // ==================== Testes Integrados ====================

    @Test
    void testCompleteUserLifecycle() {
        // Criar usuário ativo
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        assertTrue(user.canLogin());
        
        // Bloquear por violação
        user.block();
        assertFalse(user.canLogin());
        assertTrue(user.isBlocked());
        
        // Desbloquear após análise
        user.unblock();
        assertTrue(user.canLogin());
        assertTrue(user.isActive());
        
        // Promover para master
        user.promoteToMaster();
        assertTrue(user.isMaster());
        
        // Master pode acessar mesmo se bloqueado
        user.block();
        assertTrue(user.canLogin());
        assertTrue(user.canAccess(new ClientSystem(SystemId.of(1L), "test-client", "secret", "Test System", "https://example.com/callback", SystemStatus.ACTIVE)));
    }

    @Test
    void testPasswordChangeFlow() {
        User user = new User(userId, username, email, password, false, UserStatus.ACTIVE, name);
        
        // Verificar senha inicial
        assertTrue(user.verifyPassword("password123"));
        assertFalse(user.verifyPassword("wrongPassword"));
        
        // Trocar senha
        Password newPassword = Password.fromPlainText("newSecurePass999");
        user.changePassword(newPassword);
        
        // Verificar nova senha
        assertTrue(user.verifyPassword("newSecurePass999"));
        assertFalse(user.verifyPassword("password123"));
    }
}
