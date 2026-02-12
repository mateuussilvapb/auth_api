package com.mssousa.auth.application.service.authorization;

import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole;
import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.repository.UserSystemRepository;
import com.mssousa.auth.domain.repository.UserSystemRoleRepository;
import com.mssousa.auth.domain.repository.SystemRoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {

    private final UserSystemRepository userSystemRepository;
    private final UserSystemRoleRepository userSystemRoleRepository;
    private final SystemRoleRepository systemRoleRepository;

    @Override
    public AuthorizedUser authorize(
            UserId userId,
            SystemId systemId,
            boolean isMaster
    ) {

        // Bypass para usuários Master
        if (isMaster) {
            return new AuthorizedUser(Set.of("MASTER"));
        }

        // Busca vínculo usuário <-> sistema. Se não localizado, lança exceção.
        UserSystem userSystem = userSystemRepository
                .findByUserIdAndSystemId(userId, systemId)
                .orElseThrow(() -> accessDenied());

        // Se o vínculo existir, mas não estiver ativo, lança exceção.
        if (!userSystem.isActive()) {
            throw accessDenied();
        }

        // Busca o bind de usuário <-> sistema <-> role
        var bindings = userSystemRoleRepository
                .findByUserSystemId(userSystem.getId());

        /* 
         * 1. Filtra apenas o binds ativos de usuário <-> sistema <-> role;
         * 2. Coleta apenas os ids;
         * 3. Transforma o resultado em uma coleção.
         */ 
        var activeRoleIds = bindings.stream()
                .filter(UserSystemRole::isActive)
                .map(UserSystemRole::getSystemRoleId)
                .collect(Collectors.toSet());

        // Acesso negado, caso não exista nenhuma role que atenda os critérios anteriores.
        if (activeRoleIds.isEmpty()) {
            throw accessDenied();
        }

        // Busca todas as roles filtradas anteriormente.
        var roles = systemRoleRepository.findAllById(activeRoleIds);

        // Filtra apenas as roles ativas e coleta apenas os códigos.
        Set<String> roleCodes = roles.stream()
                .filter(SystemRole::isActive)
                .map(SystemRole::getCode)
                .collect(Collectors.toSet());

        // Acesso negado, caso não exista nenhuma role que atenda os critérios anteriores.
        if (roleCodes.isEmpty()) {
            throw accessDenied();
        }

        // Retorna as roles autorizadas.
        return new AuthorizedUser(roleCodes);
    }

    private OAuth2AuthenticationException accessDenied() {
        return new OAuth2AuthenticationException(
                new OAuth2Error("access_denied")
        );
    }
}
