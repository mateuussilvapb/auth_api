package com.mssousa.auth.infrastructure.security.oauth2;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;

/**
 * Token de autenticação customizado que transporta os dados do usuário pelo pipeline do Spring Security.
 * 
 * Responsabilidades:
 * - Representar o estado de autenticação em dois momentos distintos do fluxo OAuth2
 * - Encapsular as credenciais (login/senha) antes da autenticação
 * - Encapsular o AuthenticatedUser após a autenticação bem-sucedida
 * - Servir como veículo de dados entre CustomAuthenticationProvider e JwtTokenCustomizer
 * 
 * Dois estados possíveis:
 * - NÃO AUTENTICADO: criado via construtor público com login e senha,
 *   usado pelo filtro de autenticação para iniciar o fluxo
 * - AUTENTICADO: criado via factory method authenticated() com AuthenticatedUser,
 *   usado pelo provider após validação bem-sucedida das credenciais
 * 
 * Uso no fluxo:
 * 1. O filtro OAuth2 cria um token não autenticado (login + senha)
 * 2. O CustomAuthenticationProvider recebe e valida via AuthenticationService
 * 3. Em caso de sucesso, cria um token autenticado via authenticated()
 * 4. O JwtTokenCustomizer extrai o AuthenticatedUser deste token para gerar claims JWT
 */
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser principal;
    private final Object credentials;

    /**
     * Construtor para token NÃO autenticado.
     * Usado antes da validação das credenciais.
     */
    public CustomAuthenticationToken(String login, String password) {
        super(Collections.emptyList());
        this.principal = null;
        this.credentials = password;
        setAuthenticated(false);
        setDetails(login);
    }

    /**
     * Construtor para token AUTENTICADO.
     * Deve ser usado apenas pelo AuthenticationProvider.
     */
    private CustomAuthenticationToken(
            AuthenticatedUser principal,
            Collection<? extends GrantedAuthority> authorities) {

        super(authorities);
        this.principal = principal;
        this.credentials = null;
        setAuthenticated(true);
    }

    /**
     * Factory method para criação segura do token autenticado.
     */
    public static CustomAuthenticationToken authenticated(AuthenticatedUser user) {
        return new CustomAuthenticationToken(
            user,
            Collections.emptyList()
        );
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
