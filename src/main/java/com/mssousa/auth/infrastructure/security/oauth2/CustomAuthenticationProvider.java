package com.mssousa.auth.infrastructure.security.oauth2;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.mssousa.auth.application.service.authentication.AuthenticationService;
import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;

import lombok.RequiredArgsConstructor;

/**
 * Provider de autenticação customizado que integra o Spring Security com a camada de aplicação.
 * 
 * Responsabilidades:
 * - Servir como ponte entre o fluxo OAuth2 do Spring Security e o AuthenticationService da aplicação
 * - Extrair login e senha do Authentication recebido pelo framework
 * - Delegar a validação de credenciais ao AuthenticationService (camada de aplicação)
 * - Converter o resultado em um CustomAuthenticationToken autenticado
 * - Traduzir exceções da aplicação em BadCredentialsException do Spring Security
 * 
 * Funcionamento:
 * - Registrado no AuthenticationManager via configuração de segurança
 * - Invocado automaticamente pelo Spring Security quando recebe um CustomAuthenticationToken
 * - O método supports() garante que este provider só processa CustomAuthenticationToken
 * - Em caso de sucesso, retorna um CustomAuthenticationToken autenticado com os dados do usuário
 * - Em caso de falha, lança BadCredentialsException genérica (segurança)
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            AuthenticatedUser user =
                authenticationService.authenticate(login, password);

            return CustomAuthenticationToken.authenticated(user);

        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}