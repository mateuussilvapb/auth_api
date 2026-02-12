package com.mssousa.auth.infrastructure.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;
import com.mssousa.auth.application.service.authorization.UserAuthorizationService;
import com.mssousa.auth.application.service.client.ClientValidationService;
import com.mssousa.auth.infrastructure.security.oauth2.CustomAuthenticationToken;

import lombok.RequiredArgsConstructor;

/**
 * Customizador de tokens JWT para access tokens gerados pelo Spring Authorization Server.
 * 
 * Responsabilidades:
 * - Interceptar a geração de tokens JWT via OAuth2TokenCustomizer
 * - Filtrar apenas access_tokens (ignorar refresh_token, id_token, etc.)
 * - Extrair os dados do usuário autenticado a partir do CustomAuthenticationToken
 * - Adicionar claims customizados ao JWT (subject, username, email, name, is_master)
 * 
 * Funcionamento:
 * - Registrado como @Bean na configuração de segurança
 * - Invocado automaticamente pelo Spring Authorization Server durante a emissão de tokens
 * - Recebe o JwtEncodingContext contendo o Authentication e o builder de claims
 * - Só atua quando o token é do tipo access_token E o principal é um CustomAuthenticationToken
 */
@RequiredArgsConstructor
public class JwtTokenCustomizer
        implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final ClientValidationService clientValidationService;
    private final UserAuthorizationService userAuthorizationService;

    @Override
    public void customize(JwtEncodingContext context) {

        if (!"access_token".equals(context.getTokenType().getValue())) {
            return;
        }

        Authentication authentication = context.getPrincipal();

        if (!(authentication instanceof CustomAuthenticationToken customToken)) {
            return;
        }

        AuthenticatedUser user =
                (AuthenticatedUser) customToken.getPrincipal();

        String clientId = context.getRegisteredClient().getClientId();

        // Valida se client está ativo
        var client =
                clientValidationService.validateActiveClient(clientId);

        // Autoriza usuário no sistema
        var authorizedUser =
                userAuthorizationService.authorize(
                        user.userId(),
                        client.getId(),
                        user.master()
                );

        // Adiciona claims ao token
        context.getClaims()
            .subject(user.userId().value().toString())
            .claim("client_id", client.getClientId())
            .claim("username", user.username())
            .claim("email", user.email())
            .claim("name", user.name())
            .claim("is_master", user.master())
            .claim("roles", authorizedUser.roles());
    }
}

