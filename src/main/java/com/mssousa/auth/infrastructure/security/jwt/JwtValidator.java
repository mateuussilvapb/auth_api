package com.mssousa.auth.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.token.JwtPayload;
import com.mssousa.auth.domain.model.user.UserId;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Validador de tokens JWT.
 * Responsável por validar tokens JWT e extrair o payload.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtProperties jwtProperties;

    /**
     * Valida um token JWT e extrai o payload.
     *
     * @param token Token JWT a ser validado
     * @return Optional contendo o JwtPayload se válido, vazio se inválido
     */
    public Optional<JwtPayload> validateAndExtract(String token) {
        try {
            SecretKey key = getSigningKey();

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Validar versão do token
            Integer tokenVersion = claims.get("tokenVersion", Integer.class);
            if (tokenVersion == null || tokenVersion != jwtProperties.getTokenVersion()) {
                log.warn("Token version mismatch. Expected: {}, Got: {}", 
                        jwtProperties.getTokenVersion(), tokenVersion);
                return Optional.empty();
            }

            JwtPayload payload = extractPayload(claims);
            log.debug("JWT validated successfully for user: {}", payload.username());
            return Optional.of(payload);

        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            return Optional.empty();
        } catch (JwtException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Extrai o JwtPayload dos claims do token.
     */
    private JwtPayload extractPayload(Claims claims) {
        // Claims RFC padrão
        String issuer = claims.getIssuer();
        String subject = claims.getSubject();
        String audience = claims.getAudience().iterator().next();
        Instant issuedAt = claims.getIssuedAt().toInstant();
        Instant expiresAt = claims.getExpiration().toInstant();
        String jwtId = claims.getId();

        // Claims customizados
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        Boolean master = claims.get("master", Boolean.class);
        Long systemId = claims.get("systemId", Long.class);
        
        @SuppressWarnings("unchecked")
        List<String> systemRoles = claims.get("systemRoles", List.class);
        
        String authMethod = claims.get("authMethod", String.class);
        String sessionId = claims.get("sessionId", String.class);
        Integer tokenVersion = claims.get("tokenVersion", Integer.class);

        return JwtPayload.builder()
                .issuer(issuer)
                .subject(subject)
                .audience(audience)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .jwtId(jwtId)
                .userId(UserId.of(userId))
                .username(username)
                .email(email)
                .name(name)
                .master(master)
                .systemId(SystemId.of(systemId))
                .systemRoles(systemRoles)
                .authMethod(authMethod)
                .sessionId(sessionId)
                .tokenVersion(tokenVersion)
                .build();
    }

    /**
     * Cria a chave de assinatura a partir do secret configurado.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Valida apenas se o token é válido (assinatura e expiração).
     * Não extrai o payload completo.
     *
     * @param token Token JWT a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean isValid(String token) {
        return validateAndExtract(token).isPresent();
    }
}
