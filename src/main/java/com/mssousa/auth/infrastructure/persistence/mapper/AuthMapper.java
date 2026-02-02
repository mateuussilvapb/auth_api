package com.mssousa.auth.infrastructure.persistence.mapper;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

import com.mssousa.auth.domain.model.binding.BindingStatus;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRoleId;
import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.model.role.SystemRoleStatus;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.system.SystemStatus;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCode;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCodeId;
import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetToken;
import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetTokenId;
import com.mssousa.auth.domain.model.token.passwordResetToken.ResetTokenValue;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.infrastructure.persistence.entity.AuthorizationCodeEntity;
import com.mssousa.auth.infrastructure.persistence.entity.BaseJpaEntity;
import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.mssousa.auth.infrastructure.persistence.entity.SystemRoleEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserSystemRoleEntity;

@Component
public class AuthMapper {

    /**
     * Define o ID de uma entidade JPA a partir do ID já existente no domínio.
     *
     * ⚠️ Regra arquitetural:
     * - O ID DEVE ser gerado no Application Service.
     * - O domínio DEVE possuir identidade antes da persistência.
     * - A camada de persistência NÃO gera identidade.
     *
     * @param entity A entidade JPA que terá o ID definido
     * @param id     O valor do ID a ser definido
     */
    private void setEntityId(BaseJpaEntity entity, Long id) {
        if (id == null) {
            throw new IllegalStateException(
                "ID da entidade JPA não pode ser nulo. " +
                "O ID deve ser gerado no Application Service antes da persistência."
            );
        }

        try {
            Field idField = BaseJpaEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Falha ao definir ID da entidade", e);
        }
    }

    // ==================== User ====================

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
            .id(UserId.of(entity.getId()))
            .username(Username.of(entity.getUsername()))
            .email(Email.of(entity.getEmail()))
            .password(Password.fromHash(entity.getPasswordHash()))
            .master(entity.isMaster())
            .status(UserStatus.valueOf(entity.getStatus()))
            .name(entity.getName())
            .build();
    }

    public UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        setEntityId(entity, user.getId() != null ? user.getId().value() : null);
        
        entity.setUsername(user.getUsername().value());
        entity.setEmail(user.getEmail().value());
        entity.setPasswordHash(user.getPassword().hashedValue());
        entity.setName(user.getName());
        entity.setMaster(user.isMaster());
        entity.setStatus(user.getStatus().name());
        return entity;
    }

    // ==================== ClientSystem ====================

    public ClientSystem toDomain(ClientSystemEntity entity) {
        if (entity == null) return null;
        return ClientSystem.builder()
            .id(SystemId.of(entity.getId()))
            .clientId(entity.getClientId())
            .clientSecret(entity.getClientSecret())
            .name(entity.getName())
            .redirectUri(entity.getRedirectUri())
            .status(SystemStatus.valueOf(entity.getStatus()))
            .build();
    }

    public ClientSystemEntity toEntity(ClientSystem system) {
        if (system == null) return null;
        ClientSystemEntity entity = new ClientSystemEntity();
        setEntityId(entity, system.getId() != null ? system.getId().value() : null);
        
        entity.setClientId(system.getClientId());
        entity.setClientSecret(system.getClientSecret());
        entity.setName(system.getName());
        entity.setRedirectUri(system.getRedirectUri());
        entity.setStatus(system.getStatus().name());
        return entity;
    }

    // ==================== SystemRole ====================

    public SystemRole toDomain(SystemRoleEntity entity) {
        if (entity == null) return null;
        return SystemRole.builder()
            .id(SystemRoleId.of(entity.getId()))
            .system_id(SystemId.of(entity.getSystem().getId()))
            .code(entity.getCode())
            .description(entity.getDescription())
            .status(SystemRoleStatus.valueOf(entity.getStatus()))
            .build();
    }

    public SystemRoleEntity toEntity(SystemRole role, ClientSystemEntity systemEntity) {
        if (role == null) return null;
        SystemRoleEntity entity = new SystemRoleEntity();
        setEntityId(entity, role.getId() != null ? role.getId().value() : null);
        
        entity.setSystem(systemEntity);
        entity.setCode(role.getCode());
        entity.setDescription(role.getDescription());
        entity.setStatus(role.getStatus().name());
        return entity;
    }

    // ==================== UserSystem ====================

    public UserSystem toDomain(UserSystemEntity entity) {
        if (entity == null) return null;
        return UserSystem.builder()
            .id(UserSystemId.of(entity.getId()))
            .userId(UserId.of(entity.getUser().getId()))
            .systemId(SystemId.of(entity.getSystem().getId()))
            .status(BindingStatus.valueOf(entity.getStatus()))
            .build();
    }

    public UserSystemEntity toEntity(UserSystem userSystem, UserEntity user, ClientSystemEntity system) {
        if (userSystem == null) return null;
        UserSystemEntity entity = new UserSystemEntity();
        setEntityId(entity, userSystem.getId() != null ? userSystem.getId().value() : null);
        
        entity.setUser(user);
        entity.setSystem(system);
        entity.setStatus(userSystem.getStatus().name());
        return entity;
    }

    // ==================== UserSystemRole ====================

    public UserSystemRole toDomain(UserSystemRoleEntity entity) {
        if (entity == null) return null;
        return UserSystemRole.builder()
            .id(UserSystemRoleId.of(entity.getId()))
            .userSystemId(UserSystemId.of(entity.getUserSystem().getId()))
            .systemRoleId(SystemRoleId.of(entity.getSystemRole().getId()))
            .status(BindingStatus.valueOf(entity.getStatus()))
            .build();
    }

    public UserSystemRoleEntity toEntity(UserSystemRole userSystemRole, UserSystemEntity userSystem, SystemRoleEntity systemRole) {
        if (userSystemRole == null) return null;
        UserSystemRoleEntity entity = new UserSystemRoleEntity();
        setEntityId(entity, userSystemRole.getId() != null ? userSystemRole.getId().value() : null);
        
        entity.setUserSystem(userSystem);
        entity.setSystemRole(systemRole);
        entity.setStatus(userSystemRole.getStatus().name());
        return entity;
    }

    // ==================== AuthorizationCode ====================

    public AuthorizationCode toDomain(AuthorizationCodeEntity entity) {
        if (entity == null) return null;
        return AuthorizationCode.builder()
            .id(AuthorizationCodeId.of(entity.getId()))
            .code(entity.getCode())
            .user(toDomain(entity.getUser()))
            .systemId(SystemId.of(entity.getSystem().getId()))
            .expiresAt(entity.getExpiresAt())
            .used(entity.isUsed())
            .build();
    }

    public AuthorizationCodeEntity toEntity(AuthorizationCode authCode, UserEntity user, ClientSystemEntity system) {
        if (authCode == null) return null;
        AuthorizationCodeEntity entity = new AuthorizationCodeEntity();
        setEntityId(entity, authCode.getId() != null ? authCode.getId().value() : null);
        
        entity.setCode(authCode.getCode());
        entity.setUser(user); // Define o relacionamento
        entity.setSystem(system); // Define o relacionamento
        entity.setExpiresAt(authCode.getExpiresAt());
        entity.setUsed(authCode.getUsed());
        return entity;
    }

    // ==================== PasswordResetToken ====================

    public PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        if (entity == null) return null;
        return new PasswordResetToken(
            PasswordResetTokenId.of(entity.getId()),
            new ResetTokenValue(entity.getToken()),
            UserId.of(entity.getUser().getId()),
            entity.getExpiresAt(),
            entity.isUsed()
        );
    }

    public PasswordResetTokenEntity toEntity(PasswordResetToken token, UserEntity user) {
        if (token == null) return null;
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        setEntityId(entity, token.getId() != null ? token.getId().value() : null);
        
        entity.setToken(token.getValue().value());
        entity.setUser(user);
        entity.setExpiresAt(token.getExpiresAt());
        entity.setUsed(token.getUsed());
        return entity;
    }
}
