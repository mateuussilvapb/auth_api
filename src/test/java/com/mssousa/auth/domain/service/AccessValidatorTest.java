
package com.mssousa.auth.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.system.SystemStatus;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;

@DisplayName("AccessValidator - Domain Service Test")
class AccessValidatorTest {

    private AccessValidator accessValidator;

    @BeforeEach
    void setUp() {
        accessValidator = new AccessValidator();
    }

    @Nested
    @DisplayName("Usuário MASTER")
    class MasterUserTests {

        @Test
        @DisplayName("Deve permitir acesso de usuário MASTER sem vínculo")
        void shouldAllowMasterUserWithoutBinding() {
            // Arrange
            User masterUser = createMasterUser();
            ClientSystem system = createActiveSystem();

            // Act & Assert
            accessValidator.validateAccess(masterUser, system, null);
            assertThat(accessValidator.canAccess(masterUser, system, null)).isTrue();
        }

        @Test
        @DisplayName("Deve bloquear usuário MASTER inativo")
        void shouldBlockInactiveMasterUser() {
            // Arrange
            User masterUser = createMasterUser();
            masterUser.disable();
            ClientSystem system = createActiveSystem();

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(masterUser, system, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("inativo");
        }

        @Test
        @DisplayName("Deve bloquear usuário MASTER bloqueado")
        void shouldBlockBlockedMasterUser() {
            // Arrange
            User masterUser = createMasterUser();
            masterUser.block();
            ClientSystem system = createActiveSystem();

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(masterUser, system, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("bloqueado");
        }
    }

    @Nested
    @DisplayName("Usuário Normal")
    class RegularUserTests {

        @Test
        @DisplayName("Deve permitir acesso de usuário normal com vínculo ativo")
        void shouldAllowRegularUserWithActiveBinding() {
            // Arrange
            User user = createRegularUser();
            ClientSystem system = createActiveSystem();
            UserSystem binding = createActiveBinding(user, system);

            // Act & Assert
            accessValidator.validateAccess(user, system, binding);
            assertThat(accessValidator.canAccess(user, system, binding)).isTrue();
        }

        @Test
        @DisplayName("Deve bloquear usuário normal sem vínculo")
        void shouldBlockRegularUserWithoutBinding() {
            // Arrange
            User user = createRegularUser();
            ClientSystem system = createActiveSystem();

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("não possui vínculo");
        }

        @Test
        @DisplayName("Deve bloquear usuário normal inativo")
        void shouldBlockInactiveUser() {
            // Arrange
            User user = createRegularUser();
            user.disable();
            ClientSystem system = createActiveSystem();
            UserSystem binding = createActiveBinding(user, system);

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, binding))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("inativo");
        }

        @Test
        @DisplayName("Deve bloquear usuário normal bloqueado")
        void shouldBlockBlockedUser() {
            // Arrange
            User user = createRegularUser();
            user.block();
            ClientSystem system = createActiveSystem();
            UserSystem binding = createActiveBinding(user, system);

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, binding))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("bloqueado");
        }
    }

    @Nested
    @DisplayName("Validação de Sistema")
    class SystemValidationTests {

        @Test
        @DisplayName("Deve bloquear acesso a sistema inativo")
        void shouldBlockAccessToInactiveSystem() {
            // Arrange
            User user = createRegularUser();
            ClientSystem system = createActiveSystem();
            system.deactivate();
            UserSystem binding = createActiveBinding(user, system);

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, binding))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Sistema")
                    .hasMessageContaining("inativo");
        }
    }

    @Nested
    @DisplayName("Validação de Vínculo")
    class BindingValidationTests {

        @Test
        @DisplayName("Deve bloquear vínculo inativo")
        void shouldBlockInactiveBinding() {
            // Arrange
            User user = createRegularUser();
            ClientSystem system = createActiveSystem();
            UserSystem binding = createActiveBinding(user, system);
            binding.deactivate();

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, binding))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Vínculo")
                    .hasMessageContaining("inativo");
        }

        @Test
        @DisplayName("Deve bloquear vínculo bloqueado")
        void shouldBlockBlockedBinding() {
            // Arrange
            User user = createRegularUser();
            ClientSystem system = createActiveSystem();
            UserSystem binding = createActiveBinding(user, system);
            binding.block();

            // Act & Assert
            assertThatThrownBy(() -> accessValidator.validateAccess(user, system, binding))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Vínculo")
                    .hasMessageContaining("bloqueado");
        }
    }

    // Helper methods
    private User createMasterUser() {
        return User.builder()
        .id(UserId.of(1L))
        .username(Username.of("admin"))
        .email(Email.of("admin@test.com"))
        .password(Password.fromPlainText("Password123"))
        .master(true)
        .status(UserStatus.ACTIVE)
        .name("Admin User")
        .build();
    }

    private User createRegularUser() {
        return User.builder()
        .id(UserId.of(2L))
        .username(Username.of("user"))
        .email(Email.of("user@test.com"))
        .password(Password.fromPlainText("Password123"))
        .master(false)
        .status(UserStatus.ACTIVE)
        .name("Regular User")
        .build();
    }

    private ClientSystem createActiveSystem() {
        return new ClientSystem(
                SystemId.of(100L),
                "test-client-id",
                "test-secret-123",
                "Test System",
                "https://example.com/callback",
                SystemStatus.ACTIVE);
    }

    private UserSystem createActiveBinding(User user, ClientSystem system) {
        return UserSystem.builder()
                .id(UserSystemId.of(1L))
                .userId(user.getId())
                .systemId(system.getId())
                .status(BindingStatus.ACTIVE)
                .build();
    }
}
