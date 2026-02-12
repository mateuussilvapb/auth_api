package com.mssousa.auth.infrastructure.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;
import com.mssousa.auth.application.service.authorization.UserAuthorizationService;
import com.mssousa.auth.application.service.client.ClientValidationService;
import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.infrastructure.security.oauth2.CustomAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class JwtTokenCustomizerTest {

    @Mock
    private ClientValidationService clientValidationService;

    @Mock
    private UserAuthorizationService userAuthorizationService;

    @Mock
    private JwtEncodingContext context;

    @Mock
    private RegisteredClient registeredClient;

    @Mock
    private JwtClaimsSet.Builder claimsBuilder;

    @InjectMocks
    private JwtTokenCustomizer customizer;

    @Test
    void shouldDoNothingIfNotAccessToken() {

        when(context.getTokenType())
                .thenReturn(new OAuth2TokenType("refresh_token"));

        customizer.customize(context);

        verifyNoInteractions(clientValidationService);
    }

    @Test
    void shouldCustomizeTokenCorrectly() {
        when(context.getClaims()).thenReturn(claimsBuilder);

        when(claimsBuilder.subject(anyString()))
                .thenReturn(claimsBuilder);

        when(claimsBuilder.claim(anyString(), any()))
                .thenReturn(claimsBuilder);

        when(context.getTokenType())
                .thenReturn(OAuth2TokenType.ACCESS_TOKEN);

        when(context.getRegisteredClient())
                .thenReturn(registeredClient);

        when(registeredClient.getClientId())
                .thenReturn("client-1");

        var user = mock(AuthenticatedUser.class);
        when(user.userId()).thenReturn(UserId.of(1L));
        when(user.username()).thenReturn(Username.of("mateus"));
        when(user.email()).thenReturn(Email.of("mateus@email.com"));
        when(user.name()).thenReturn("Mateus");
        when(user.master()).thenReturn(false);

        var authentication = CustomAuthenticationToken.authenticated(user);

        when(context.getPrincipal())
                .thenReturn(authentication);

        var client = mock(ClientSystem.class);
        when(client.getId()).thenReturn(SystemId.of(10L));
        when(client.getClientId()).thenReturn("client-1");

        when(clientValidationService.validateActiveClient("client-1"))
                .thenReturn(client);

        when(userAuthorizationService.authorize(
                any(), any(), anyBoolean()))
                .thenReturn(new UserAuthorizationService.AuthorizedUser(
                        Set.of("ADMIN")));

        customizer.customize(context);

        verify(claimsBuilder).subject("1");
        verify(claimsBuilder).claim("client_id", "client-1");
        verify(claimsBuilder).claim("roles", Set.of("ADMIN"));
    }
}
