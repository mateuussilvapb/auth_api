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
     * Define o ID de uma entidade JPA usando reflexão.
     * <p>
     * ⚠️ USO INTERNO: Este método é necessário porque não utilizamos setId() público em {@link com.mssousa.auth.infrastructure.persistence.entity.BaseJpaEntity}.
     * para preservar a imutabilidade do ID. Ele é usado APENAS para reconstruir
     * entidades JPA que representam objetos de domínio já persistidos (update).
     * </p>
     * <p>
     * Para novas entidades, o ID será gerado automaticamente pelo @PrePersist.
     * </p>
     *
     * @param entity A entidade JPA que terá o ID definido
     * @param id     O valor do ID a ser definido
     */
    private void setEntityId(BaseJpaEntity entity, Long id) {
        if (id == null) {
            return; // Não faz nada se ID for null - será gerado pelo @PrePersist
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
        return new User(
            new UserId(entity.getId()),
            new Username(entity.getUsername()),
            new Email(entity.getEmail()),
            Password.fromHash(entity.getPasswordHash()),
            entity.isMaster(),
            UserStatus.valueOf(entity.getStatus()),
            entity.getName()
        );
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
        return new ClientSystem(
            new SystemId(entity.getId()),
            entity.getClientId(),
            entity.getClientSecret(),
            entity.getName(),
            entity.getRedirectUri(),
            SystemStatus.valueOf(entity.getStatus())
        );
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
        return new SystemRole(
            new SystemRoleId(entity.getId()),
            new SystemId(entity.getSystem().getId()),
            entity.getCode(),
            entity.getDescription(),
            SystemRoleStatus.valueOf(entity.getStatus())
        );
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
        return new UserSystem(
            new UserSystemId(entity.getId()),
            new UserId(entity.getUser().getId()),
            new SystemId(entity.getSystem().getId()),
            BindingStatus.valueOf(entity.getStatus())
        );
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
        return new UserSystemRole(
            new UserSystemRoleId(entity.getId()),
            new UserSystemId(entity.getUserSystem().getId()),
            new SystemRoleId(entity.getSystemRole().getId()),
            BindingStatus.valueOf(entity.getStatus())
        );
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
        return new AuthorizationCode(
            new AuthorizationCodeId(entity.getId()),
            entity.getCode(),
            toDomain(entity.getUser()), // Reutiliza o mapper de User
            new SystemId(entity.getSystem().getId()),
            entity.getExpiresAt(),
            entity.isUsed()
        );
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
            new PasswordResetTokenId(entity.getId()),
            new ResetTokenValue(entity.getToken()),
            new UserId(entity.getUser().getId()),
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
