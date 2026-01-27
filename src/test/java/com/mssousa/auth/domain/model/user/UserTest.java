package com.mssousa.auth.domain.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;

class UserTest {

    private UserId userId;
    private Username username;
    private Email email;
    private Password password;
    private String name;

    @BeforeEach
    void setUp() {
        userId = UserId.of(1L);
        username = Username.of("testuser");
        email = Email.of("test@example.com");
        password = Password.fromPlainText("password123");
        name = "Test User";
    }

    // ==================== Criação e Getters ====================

    @Test
    void testCreateUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

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
    void testCreateUserWithNullId() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(null)
                        .username(username)
                        .email(email)
                        .password(password)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name(name)
                        .build());
        assertEquals(User.DEFAULT_ERROR_ID, exception.getMessage());
    }

    @Test
    void testCreateUserWithNullUsername() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(userId)
                        .username(null)
                        .email(email)
                        .password(password)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name(name)
                        .build());
        assertEquals(Username.DEFAULT_ERROR_USERNAME, exception.getMessage());
    }

    @Test
    void testCreateUserWithNullEmail() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(userId)
                        .username(username)
                        .email(null)
                        .password(password)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name(name)
                        .build());
        assertEquals(Email.DEFAULT_ERROR_EMAIL, exception.getMessage());
    }

    @Test
    void testCreateUserWithNullPassword() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(userId)
                        .username(username)
                        .email(email)
                        .password(null)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name(name)
                        .build());
        assertEquals(Password.DEFAULT_ERROR_PASSWORD, exception.getMessage());
    }

    @Test
    void testCreateUserWithNullName() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(userId)
                        .username(username)
                        .email(email)
                        .password(password)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name(null)
                        .build());
        assertEquals(User.DEFAULT_ERROR_NAME, exception.getMessage());
    }

    @Test
    void testCreateUserWithBlankName() {
        DomainException exception = assertThrows(DomainException.class,
                () -> User.builder()
                        .id(userId)
                        .username(username)
                        .email(email)
                        .password(password)
                        .master(false)
                        .status(UserStatus.ACTIVE)
                        .name("  ")
                        .build());
        assertEquals(User.DEFAULT_ERROR_NAME, exception.getMessage());
    }

    @Test
    void testCreateMasterUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(user.isMaster());
    }

    @Test
    void testGetId() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertEquals(userId, user.getId());
    }

    @Test
    void testGetUsername() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertEquals(username, user.getUsername());
    }

    @Test
    void testGetEmail() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertEquals(email, user.getEmail());
    }

    @Test
    void testGetName() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertEquals(name, user.getName());
    }

    // ==================== Atualização de Perfil ====================

    @Test
    void testUpdateProfile() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        String newName = "Updated Name";
        String newEmail = "updated@example.com";

        user.updateName(newName);
        user.updateEmail(Email.of(newEmail));

        assertEquals(newName, user.getName());
        assertEquals(Email.of(newEmail), user.getEmail());
    }

    @Test
    void testUpdateNameWithNulls() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> user.updateName(null));
        assertEquals(User.DEFAULT_ERROR_NAME, exception.getMessage());
    }

    @Test
    void testUpdateNameWithEmptyStrings() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> user.updateName(""));
        assertEquals(User.DEFAULT_ERROR_NAME, exception.getMessage());
    }

    @Test
    void testUpdateEmailWithNulls() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> user.updateEmail(null));
        assertEquals(Email.DEFAULT_ERROR_EMAIL, exception.getMessage());
    }

    @Test
    void testUpdateEmailWithEmptyStrings() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> user.updateEmail(Email.of("")));
        assertEquals(Email.DEFAULT_ERROR_EMAIL, exception.getMessage());
    }

    // ==================== Gerenciamento de Status ====================

    @Test
    void testActivateUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();

        user.activate();

        assertTrue(user.isActive());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void testActivateAlreadyActiveUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        user.activate(); // Não deve lançar exceção

        assertTrue(user.isActive());
    }

    @Test
    void testBlockUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        user.block();

        assertTrue(user.isBlocked());
        assertEquals(UserStatus.BLOCKED, user.getStatus());
    }

    @Test
    void testDisableUser() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        user.disable();

        assertTrue(user.isDisabled());
        assertEquals(UserStatus.DISABLED, user.getStatus());
    }

    @Test
    void testIsActive() {
        User activeUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        User blockedUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();

        assertTrue(activeUser.isActive());
        assertFalse(blockedUser.isActive());
    }

    @Test
    void testIsBlocked() {
        User blockedUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();
        User activeUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(blockedUser.isBlocked());
        assertFalse(activeUser.isBlocked());
    }

    @Test
    void testIsDisabled() {
        User disabledUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.DISABLED)
                .name(name)
                .build();
        User activeUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(disabledUser.isDisabled());
        assertFalse(activeUser.isDisabled());
    }

    @Test
    void testStatusTransitions() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        // ACTIVE -> BLOCKED
        user.block();
        assertTrue(user.isBlocked());

        // BLOCKED -> DISABLED
        user.disable();
        assertTrue(user.isDisabled());

        // DISABLED -> ACTIVE
        user.activate();
        assertTrue(user.isActive());
    }

    // ==================== Gerenciamento de Senha ====================

    @Test
    void testChangePassword() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        Password newPassword = Password.fromPlainText("newPassword456");

        user.changePassword(newPassword);

        assertEquals(newPassword, user.getPassword());
        assertTrue(user.verifyPassword("newPassword456"));
    }

    @Test
    void testChangePasswordWithNull() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> user.changePassword(null));
        assertEquals(Password.DEFAULT_ERROR_PASSWORD, exception.getMessage());
    }

    @Test
    void testVerifyCorrectPassword() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(user.verifyPassword("password123"));
    }

    @Test
    void testVerifyIncorrectPassword() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertFalse(user.verifyPassword("wrongPassword"));
    }

    @Test
    void testVerifyPasswordWithNull() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertFalse(user.verifyPassword(null));
    }

    // ==================== Gerenciamento Master ====================

    @Test
    void testPromoteToMaster() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertFalse(user.isMaster());

        user.promoteToMaster();

        assertTrue(user.isMaster());
    }

    @Test
    void testDemoteFromMaster() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertTrue(user.isMaster());

        user.demoteFromMaster();

        assertFalse(user.isMaster());
    }

    @Test
    void testMasterCanLogin() {
        User master = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();

        assertTrue(master.canLogin());
    }

    @Test
    void testNonMasterRequiresActiveStatus() {
        User blockedUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();
        User activeUser = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertFalse(blockedUser.canLogin());
        assertTrue(activeUser.canLogin());
    }

    // ==================== Controle de Acesso ====================

    @Test
    void testCanAccessWhenActive() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(user.canLogin());
    }

    @Test
    void testCannotAccessWhenBlocked() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();

        assertFalse(user.canLogin());
    }

    @Test
    void testCannotAccessWhenDisabled() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.DISABLED)
                .name(name)
                .build();

        assertFalse(user.canLogin());
    }

    @Test
    void testMasterCanAlwaysAccess() {
        User masterBlocked = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();
        User masterDisabled = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.DISABLED)
                .name(name)
                .build();
        User masterActive = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(masterBlocked.canLogin());
        assertTrue(masterDisabled.canLogin());
        assertTrue(masterActive.canLogin());
    }

    // ==================== Login ====================

    @Test
    void testCanLoginWhenActive() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

        assertTrue(user.canLogin());
    }

    @Test
    void testCannotLoginWhenBlocked() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();

        assertFalse(user.canLogin());
    }

    @Test
    void testCannotLoginWhenDisabled() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.DISABLED)
                .name(name)
                .build();

        assertFalse(user.canLogin());
    }

    @Test
    void testMasterCanAlwaysLogin() {
        User masterBlocked = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.BLOCKED)
                .name(name)
                .build();
        User masterDisabled = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(true)
                .status(UserStatus.DISABLED)
                .name(name)
                .build();

        assertTrue(masterBlocked.canLogin());
        assertTrue(masterDisabled.canLogin());
    }

    // ==================== Testes Integrados ====================

    @Test
    void testCompleteUserLifecycle() {
        // Criar usuário ativo
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();
        assertTrue(user.canLogin());

        // Bloquear por violação
        user.block();
        assertFalse(user.canLogin());
        assertTrue(user.isBlocked());

        // Promover para master
        user.promoteToMaster();
        assertTrue(user.isMaster());

        // Master pode acessar mesmo se bloqueado
        user.block();
        assertTrue(user.canLogin());
    }

    @Test
    void testPasswordChangeFlow() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(UserStatus.ACTIVE)
                .name(name)
                .build();

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

    // ==================== Testes de Builder ====================

    @Test
    @DisplayName("Builder deve usar UserStatus.ACTIVE como padrão quando status é null")
    void testBuilderSetsActiveStatusWhenNullProvided() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .master(false)
                .status(null)
                .name(name)
                .build();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Builder deve usar UserStatus.ACTIVE como padrão quando status não é informado")
    void testBuilderDefaultsToActiveStatus() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .name(name)
                // Sem definir .status()
                .build();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    @DisplayName("Builder deve usar master=false como padrão quando não informado")
    void testBuilderDefaultsToNonMaster() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .name(name)
                // Sem definir .master()
                .build();

        assertFalse(user.isMaster());
    }

    // ==================== Testes de Validação de Status null ====================

    @Test
    @DisplayName("Criar usuário com status explicitamente null no Builder usa ACTIVE")
    void testCreateUserWithExplicitNullStatus() {
        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .password(password)
                .name(name)
                .status(null) // Explicitamente null
                .build();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertFalse(user.isBlocked());
        assertFalse(user.isDisabled());
        assertTrue(user.isActive());
    }
}
