package com.mssousa.auth.application.service.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mssousa.auth.application.exception.AuthenticationFailedException;
import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.domain.repository.UserRepository;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userRepository);
    }

    private User buildUser(UserStatus status, boolean passwordMatches) {
        User user = mock(User.class);

        when(user.getId()).thenReturn(UserId.of(1L));
        when(user.getUsername()).thenReturn(Username.of("mateus"));
        when(user.getEmail()).thenReturn(Email.of("mateus@email.com"));
        when(user.getName()).thenReturn("Mateus Dias");
        when(user.getStatus()).thenReturn(status);
        when(user.isMaster()).thenReturn(false);
        when(user.verifyPassword(any())).thenReturn(passwordMatches);

        return user;
    }

    // ============================================================
    // 1️⃣ Usuário inexistente
    // ============================================================

    @Test
    @DisplayName("Deve lançar AuthenticationFailedException quando usuário não existir")
    void shouldFailWhenUserDoesNotExist() {

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticate("inexistente", "123456"));
    }

    // ============================================================
    // 2️⃣ Senha inválida
    // ============================================================

    @Test
    @DisplayName("Deve lançar AuthenticationFailedException quando senha for inválida")
    void shouldFailWhenPasswordIsInvalid() {

        User user = buildUser(UserStatus.ACTIVE, false);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticate("mateus", "senhaErrada"));
    }

    // ============================================================
    // 3️⃣ Usuário bloqueado/inativo
    // ============================================================

    @Test
    @DisplayName("Deve lançar AuthenticationFailedException quando usuário não estiver ACTIVE")
    void shouldFailWhenUserIsNotActive() {

        User user = buildUser(UserStatus.DISABLED, true);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        assertThrows(AuthenticationFailedException.class,
                () -> authenticationService.authenticate("mateus", "123456"));
    }

    // ============================================================
    // 4️⃣ Autenticação válida
    // ============================================================

    @Test
    @DisplayName("Deve autenticar com sucesso quando credenciais forem válidas")
    void shouldAuthenticateSuccessfully() {

        User user = buildUser(UserStatus.ACTIVE, true);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(user));

        AuthenticatedUser result =
                authenticationService.authenticate("mateus", "123456");

        assertNotNull(result);
        assertEquals("mateus", result.username().value());
        assertEquals("mateus@email.com", result.email().value());
        assertEquals("Mateus Dias", result.name());
        assertFalse(result.master());
    }
}
