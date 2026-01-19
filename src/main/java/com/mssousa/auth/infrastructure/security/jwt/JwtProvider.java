package com.mssousa.auth.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.mssousa.auth.domain.model.token.JwtPayload;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provedor de tokens JWT.
 * Responsável por gerar tokens JWT assinados a partir de um JwtPayload.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    /**
     * Gera um token JWT assinado a partir de um JwtPayload.
     *
     * @param payload Payload do domínio contendo dados do usuário e autorização
     * @return Token JWT assinado (String)
     */
    public String generateToken(JwtPayload payload) {
        log.debug("Generating JWT for user: {}, system: {}", 
                payload.username(), payload.systemId().value());

        SecretKey key = getSigningKey();
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(jwtProperties.getExpirationMs());

        // Claims customizados
        Map<String, Object> customClaims = Map.of(
                "userId", payload.userId().value(),
                "username", payload.username(),
                "email", payload.email(),
                "name", payload.name(),
                "master", payload.isMaster(),
                "systemId", payload.systemId().value(),
                "systemRoles", payload.systemRoles(),
                "authMethod", payload.authMethod(),
                "sessionId", payload.sessionId(),
                "tokenVersion", jwtProperties.getTokenVersion()
        );

        String token = Jwts.builder()
                // Claims RFC
                .issuer(jwtProperties.getIssuer())
                .subject(payload.subject())
                .audience().add(payload.audience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .id(UUID.randomUUID().toString())

                // Claims customizados
                .claims(customClaims)

                // Assinatura
                .signWith(key)
                .compact();

        log.debug("JWT generated successfully for user: {}", payload.username());
        return token;
    }

    /**
     * Cria a chave de assinatura a partir do secret configurado.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
