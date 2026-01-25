package com.mssousa.auth.application.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.IdGenerator;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.domain.repository.UserRepository;
import com.mssousa.auth.domain.service.EmailSender;
import com.mssousa.auth.domain.service.MasterUserPolicy;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Application Service Test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MasterUserPolicy masterUserPolicy;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private UserService userService;

    private User regularUser;
    private User masterUser;

    @BeforeEach
    void setUp() {
        regularUser = new User(
            UserId.of(1L),
            Username.of("user"),
            Email.of("user@example.com"),
            Password.fromPlainText("Pass1234"),
            false,
            UserStatus.ACTIVE,
            "Regular User"
        );

        masterUser = new User(
            UserId.of(2L),
            Username.of("admin"),
            Email.of("admin@example.com"),
            Password.fromPlainText("Pass1234"),
            true,
            UserStatus.ACTIVE,
            "Admin User"
        );
    }

    @Nested
    @DisplayName("Criação de Usuário")
    class CreateUserTests {

        @Test
        @DisplayName("Deve criar usuário comum com sucesso")
        void shouldCreateRegularUserSuccessfully() {
            // Arrange
            when(userRepository.existsByUsername(any())).thenReturn(false);
            when(userRepository.existsByEmail(any())).thenReturn(false);
            when(idGenerator.generate()).thenReturn(1L);
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            User created = userService.createUser("newuser", "new@example.com", "Pass1234", "New User");

            // Assert
            assertThat(created).isNotNull();
            assertThat(created.getUsername().value()).isEqualTo("newuser");
            verify(userRepository).save(any(User.class));
            verify(emailSender).send(any(), any(), any());
        }

        @Test
        @DisplayName("Deve criar usuário MASTER quando solicitante for MASTER")
        void shouldCreateMasterUserWhenRequesterIsMaster() {
            // Arrange
            when(masterUserPolicy.canPromoteToMaster(masterUser)).thenReturn(true);
            when(userRepository.existsByUsername(any())).thenReturn(false);
            when(userRepository.existsByEmail(any())).thenReturn(false);
            when(idGenerator.generate()).thenReturn(3L);
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            User created = userService.createUser("newadmin", "admin2@example.com", "Pass1234", "New Admin", true, masterUser);

            // Assert
            assertThat(created.isMaster()).isTrue();
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Deve falhar ao criar usuário com username duplicado")
        void shouldFailWithDuplicateUsername() {
            // Arrange
            when(userRepository.existsByUsername(any())).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> 
                userService.createUser("user", "other@example.com", "Pass1234", "User")
            ).isInstanceOf(DomainException.class)
             .hasMessageContaining("Username");
        }

        @Test
        @DisplayName("Deve falhar se não-MASTER tentar criar MASTER")
        void shouldFailIfRegularUserTriesToCreateMaster() {
            // Arrange
            if (regularUser != null) {
                 when(masterUserPolicy.canPromoteToMaster(regularUser)).thenReturn(false);
            }
            
            // Act & Assert
            assertThatThrownBy(() -> 
                userService.createUser("newmaster", "master@example.com", "Pass1234", "Master", true, regularUser)
            ).isInstanceOf(DomainException.class)
             .hasMessageContaining("Apenas usuários MASTER");
             
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Busca de Usuários (Pagination)")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar página de usuários")
        void shouldReturnPageOfUsers() {
            // Arrange
            Pageable pageable = Pageable.unpaged();
            Page<User> userPage = new PageImpl<>(List.of(regularUser));
            when(userRepository.findAll(pageable)).thenReturn(userPage);

            // Act
            Page<User> result = userService.findAll(pageable);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(regularUser);
            verify(userRepository).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("Atualização de Usuário")
    class UpdateUserTests {

        @Test
        @DisplayName("Deve atualizar nome e email com sucesso")
        void shouldUpdateUserSuccessfully() {
            // Arrange
            when(userRepository.findById(any())).thenReturn(Optional.of(regularUser));
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            User updated = userService.updateUser(1L, "New Name", "newemail@example.com");

            // Assert
            assertThat(updated.getName()).isEqualTo("New Name");
            assertThat(updated.getEmail().value()).isEqualTo("newemail@example.com");
        }

        @Test
        @DisplayName("Deve falhar ao atualizar para email duplicado")
        void shouldFailUpdateWithDuplicateEmail() {
            // Arrange
            when(userRepository.findById(any())).thenReturn(Optional.of(regularUser));
            when(userRepository.existsByEmail(Email.of("duplicate@example.com"))).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> 
                userService.updateUser(1L, "New Name", "duplicate@example.com")
            ).isInstanceOf(DomainException.class)
             .hasMessageContaining("Email");
        }
    }

    @Nested
    @DisplayName("Promoção e Rebaixamento")
    class MasterPromotionTests {

        @Test
        @DisplayName("MASTER pode promover usuário comum a MASTER")
        void masterCanPromoteUser() {
            // Arrange
            when(masterUserPolicy.canPromoteToMaster(masterUser)).thenReturn(true);
            when(masterUserPolicy.canChangeMasterStatus(masterUser, regularUser, true)).thenReturn(true);
            when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));
            when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

            // Act
            User promoted = userService.promoteToMaster(regularUser.getId().value(), masterUser);

            // Assert
            assertThat(promoted.isMaster()).isTrue();
        }

        @Test
        @DisplayName("Usuário comum não pode promover")
        void regularUserCannotPromote() {
            // Arrange
            when(masterUserPolicy.canPromoteToMaster(regularUser)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> 
                userService.promoteToMaster(2L, regularUser)
            ).isInstanceOf(DomainException.class);
        }
    }

    @Nested
    @DisplayName("Exclusão de Usuário")
    class DeleteUserTests {

        @Test
        @DisplayName("Deve excluir usuário com sucesso")
        void shouldDeleteUserSuccessfully() {
            // Arrange
            when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

            // Act
            userService.deleteUser(regularUser.getId().value());

            // Assert
            verify(userRepository).deleteById(regularUser.getId());
        }

        @Test
        @DisplayName("Deve falhar ao tentar excluir usuário inexistente")
        void shouldFailToDeleteNonExistentUser() {
            // Arrange
            Long nonExistentId = 999L;
            when(userRepository.findById(UserId.of(nonExistentId))).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> 
                userService.deleteUser(nonExistentId)
            ).isInstanceOf(DomainException.class)
             .hasMessageContaining("Usuário não encontrado");
             
            verify(userRepository, never()).deleteById(any());
        }
    }
}
