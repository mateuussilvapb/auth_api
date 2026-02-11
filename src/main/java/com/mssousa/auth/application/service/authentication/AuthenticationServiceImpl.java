package com.mssousa.auth.application.service.authentication;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mssousa.auth.application.exception.AuthenticationFailedException;
import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserStatus;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementação do serviço de autenticação.
 * 
 * Responsabilidades:
 * - Resolver login por username ou email
 * - Validar credenciais via domínio
 * - Validar status do usuário
 * - Retornar AuthenticatedUser em caso de sucesso
 * - Lançar exceção genérica em qualquer falha (segurança)
 * 
 * Princípios:
 * - Nenhuma dependência de Spring Security
 * - Nenhuma lógica de infraestrutura
 * - Falha genérica para não expor informações sensíveis
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUser authenticate(String login, String password) {

        log.debug("Attempting authentication for login: {}", login);

        User user = resolveUser(login);

        validatePassword(user, password);

        validateUserStatus(user);

        log.info("Authentication successful for user: {}", user.getUsername().value());

        return buildAuthenticatedUser(user);
    }


    /**
     * Resolve o usuário tentando por username primeiro, depois por email.
     */
    private User resolveUser(String login) {
        
        // Tentar buscar por username
        try {
            Username username = Username.of(login);
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("User not found by username: {}", login);
                        return new AuthenticationFailedException("Invalid credentials");
                    });
                    
        } catch (IllegalArgumentException usernameInvalid) {
            // Se não é um username válido, tentar como email
            log.debug("Login '{}' is not a valid username, trying as email", login);
        }
        
        // Tentar buscar por email
        try {
            Email email = Email.of(login);
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.warn("User not found by email: {}", login);
                        return new AuthenticationFailedException("Invalid credentials");
                    });
                    
        } catch (IllegalArgumentException emailInvalid) {
            // Se não é nem username nem email válido
            log.warn("Login '{}' is neither a valid username nor email", login);
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    /**
     * Valida a senha usando o método de domínio.
     */
    private void validatePassword(User user, String plainPassword) {
        
        if (plainPassword == null || plainPassword.isBlank()) {
            log.warn("Password validation failed: password is null or blank");
            throw new AuthenticationFailedException("Invalid credentials");
        }
        
        boolean passwordMatches = user.verifyPassword(plainPassword);
        
        if (!passwordMatches) {
            log.warn("Password validation failed for user: {}", user.getUsername().value());
            throw new AuthenticationFailedException("Invalid credentials");
        }
        
        log.debug("Password validated successfully for user: {}", user.getUsername().value());
    }

    /**
     * Valida se o usuário está em status ativo.
     */
    private void validateUserStatus(User user) {
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Authentication blocked: user {} has status {}", 
                    user.getUsername().value(), 
                    user.getStatus());
            throw new AuthenticationFailedException("Invalid credentials");
        }
        
        log.debug("User status validated: {}", user.getStatus());
    }

    /**
     * Constrói o DTO AuthenticatedUser a partir da entidade de domínio.
     */
    private AuthenticatedUser buildAuthenticatedUser(User user) {
        return new AuthenticatedUser(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.isMaster()
        );
    }
}