package com.mssousa.auth.application.service.authorization;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole;
import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.repository.SystemRoleRepository;
import com.mssousa.auth.domain.repository.UserSystemRepository;
import com.mssousa.auth.domain.repository.UserSystemRoleRepository;

@ExtendWith(MockitoExtension.class)
class UserAuthorizationServiceImplTest {

    @Mock
    private UserSystemRepository userSystemRepository;

    @Mock
    private UserSystemRoleRepository userSystemRoleRepository;

    @Mock
    private SystemRoleRepository systemRoleRepository;

    @InjectMocks
    private UserAuthorizationServiceImpl service;

    private final UserId userId = UserId.of(1L);
    private final SystemId systemId = SystemId.of(10L);

    @Test
    void shouldReturnMasterRoleWhenUserIsMaster() {

        var result = service.authorize(userId, systemId, true);

        assertEquals(Set.of("MASTER"), result.roles());

        verifyNoInteractions(
                userSystemRepository,
                userSystemRoleRepository,
                systemRoleRepository
        );
    }

    @Test
    void shouldThrowWhenUserSystemNotFound() {

        when(userSystemRepository.findByUserIdAndSystemId(userId, systemId))
                .thenReturn(Optional.empty());

        assertThrows(OAuth2AuthenticationException.class,
                () -> service.authorize(userId, systemId, false));
    }

    @Test
    void shouldThrowWhenUserSystemInactive() {

        var userSystem = mock(UserSystem.class);

        when(userSystem.isActive()).thenReturn(false);
        when(userSystemRepository.findByUserIdAndSystemId(userId, systemId))
                .thenReturn(Optional.of(userSystem));

        assertThrows(OAuth2AuthenticationException.class,
                () -> service.authorize(userId, systemId, false));
    }

    @Test
    void shouldThrowWhenNoActiveRolesFound() {

        var userSystem = mock(UserSystem.class);
        var userSystemId = UserSystemId.of(100L);

        when(userSystem.isActive()).thenReturn(true);
        when(userSystem.getId()).thenReturn(userSystemId);

        when(userSystemRepository.findByUserIdAndSystemId(userId, systemId))
                .thenReturn(Optional.of(userSystem));

        when(userSystemRoleRepository.findByUserSystemId(userSystemId))
                .thenReturn(List.of());

        assertThrows(OAuth2AuthenticationException.class,
                () -> service.authorize(userId, systemId, false));
    }

    @Test
    void shouldReturnRolesWhenAuthorizationIsValid() {

        var userSystem = mock(UserSystem.class);
        var userSystemId = UserSystemId.of(100L);

        when(userSystem.isActive()).thenReturn(true);
        when(userSystem.getId()).thenReturn(userSystemId);

        when(userSystemRepository.findByUserIdAndSystemId(userId, systemId))
                .thenReturn(Optional.of(userSystem));

        var binding = mock(UserSystemRole.class);
        when(binding.isActive()).thenReturn(true);
        when(binding.getSystemRoleId()).thenReturn(SystemRoleId.of(200L));

        when(userSystemRoleRepository.findByUserSystemId(userSystemId))
                .thenReturn(List.of(binding));

        var role = mock(SystemRole.class);
        when(role.isActive()).thenReturn(true);
        when(role.getCode()).thenReturn("ADMIN");

        when(systemRoleRepository.findAllById(any()))
                .thenReturn(List.of(role));

        var result = service.authorize(userId, systemId, false);

        assertEquals(Set.of("ADMIN"), result.roles());
    }
}
