package com.mssousa.auth.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;

@DisplayName("MasterUserPolicy - Domain Service Test")
class MasterUserPolicyTest {

    private MasterUserPolicy masterUserPolicy;

    @BeforeEach
    void setUp() {
        masterUserPolicy = new MasterUserPolicy();
    }

    @Nested
    @DisplayName("Identificação de MASTER")
    class MasterIdentificationTests {

        @Test
        @DisplayName("Deve identificar usuário MASTER corretamente")
        void shouldIdentifyMasterUser() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.isMaster(masterUser)).isTrue();
        }

        @Test
        @DisplayName("Deve identificar usuário normal corretamente")
        void shouldIdentifyRegularUser() {
            // Arrange
            User regularUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.isMaster(regularUser)).isFalse();
        }
    }

    @Nested
    @DisplayName("Requisitos de Roles")
    class RoleRequirementsTests {

        @Test
        @DisplayName("MASTER não deve requerer roles")
        void masterShouldNotRequireRoles() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.requiresRoles(masterUser)).isFalse();
        }

        @Test
        @DisplayName("Usuário normal deve requerer roles")
        void regularUserShouldRequireRoles() {
            // Arrange
            User regularUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.requiresRoles(regularUser)).isTrue();
        }
    }

    @Nested
    @DisplayName("Requisitos de Vínculo com Sistema")
    class SystemBindingRequirementsTests {

        @Test
        @DisplayName("MASTER não deve requerer vínculo com sistema")
        void masterShouldNotRequireSystemBinding() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.requiresSystemBinding(masterUser)).isFalse();
        }

        @Test
        @DisplayName("Usuário normal deve requerer vínculo com sistema")
        void regularUserShouldRequireSystemBinding() {
            // Arrange
            User regularUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.requiresSystemBinding(regularUser)).isTrue();
        }
    }

    @Nested
    @DisplayName("Permissões para Promoção/Rebaixamento")
    class PromotionPermissionsTests {

        @Test
        @DisplayName("MASTER pode promover outros usuários a MASTER")
        void masterCanPromoteToMaster() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.canPromoteToMaster(masterUser)).isTrue();
        }

        @Test
        @DisplayName("Usuário normal não pode promover a MASTER")
        void regularUserCannotPromoteToMaster() {
            // Arrange
            User regularUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.canPromoteToMaster(regularUser)).isFalse();
        }

        @Test
        @DisplayName("MASTER pode rebaixar outros MASTER")
        void masterCanDemoteFromMaster() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.canDemoteFromMaster(masterUser)).isTrue();
        }

        @Test
        @DisplayName("Usuário normal não pode rebaixar MASTER")
        void regularUserCannotDemoteFromMaster() {
            // Arrange
            User regularUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.canDemoteFromMaster(regularUser)).isFalse();
        }
    }

    @Nested
    @DisplayName("Alteração de Status MASTER")
    class ChangeMasterStatusTests {

        @Test
        @DisplayName("MASTER pode alterar status MASTER de qualquer usuário")
        void masterCanChangeMasterStatus() {
            // Arrange
            User masterUser = createMasterUser();
            User targetUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.canChangeMasterStatus(masterUser, targetUser, true)).isTrue();
            assertThat(masterUserPolicy.canChangeMasterStatus(masterUser, targetUser, false)).isTrue();
        }

        @Test
        @DisplayName("Usuário normal não pode alterar status MASTER")
        void regularUserCannotChangeMasterStatus() {
            // Arrange
            User regularUser = createRegularUser();
            User targetUser = createRegularUser();

            // Act & Assert
            assertThat(masterUserPolicy.canChangeMasterStatus(regularUser, targetUser, true)).isFalse();
        }

        @Test
        @DisplayName("MASTER pode alterar seu próprio status MASTER")
        void masterCanChangeSelfMasterStatus() {
            // Arrange
            User masterUser = createMasterUser();

            // Act & Assert
            assertThat(masterUserPolicy.canChangeMasterStatus(masterUser, masterUser, false)).isTrue();
        }
    }

    @Nested
    @DisplayName("Permissões Implícitas")
    class ImplicitPermissionsTests {

        @Test
        @DisplayName("MASTER deve ter permissões implícitas de acesso total")
        void masterShouldHaveFullAccessPermissions() {
            // Arrange
            User masterUser = createMasterUser();

            // Act
            String permissions = masterUserPolicy.getImplicitPermissions(masterUser);

            // Assert
            assertThat(permissions).isEqualTo("FULL_ACCESS");
        }

        @Test
        @DisplayName("Usuário normal não deve ter permissões implícitas")
        void regularUserShouldNotHaveImplicitPermissions() {
            // Arrange
            User regularUser = createRegularUser();

            // Act
            String permissions = masterUserPolicy.getImplicitPermissions(regularUser);

            // Assert
            assertThat(permissions).isEmpty();
        }
    }

    // Helper methods
    private User createMasterUser() {
        // public User(UserId id, Username username, Email email, Password password, boolean master, UserStatus status, String name) {
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
}
