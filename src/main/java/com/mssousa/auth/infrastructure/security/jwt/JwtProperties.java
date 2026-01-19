package com.mssousa.auth.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * Propriedades de configuração para JWT.
 * Carregadas do application.yml com prefixo "auth.jwt".
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    /**
     * Chave secreta para assinatura HMAC dos tokens.
     * DEVE ser mantida em segredo e rotacionada periodicamente.
     * Recomendado: 256+ bits (32+ caracteres).
     */
    @NotBlank(message = "JWT secret key não pode estar vazio")
    private String secretKey;

    /**
     * Issuer (iss) do token - identifica quem emitiu o token.
     * Exemplo: "https://auth.seudominio.com"
     */
    @NotBlank(message = "JWT issuer não pode estar vazio")
    private String issuer;

    /**
     * Tempo de expiração do token em milissegundos.
     * Padrão: 3600000 (1 hora)
     */
    @Positive(message = "JWT expiration deve ser positivo")
    private long expirationMs = 3600000L; // 1 hora

    /**
     * Versão do token (permite invalidação global).
     * Incrementar este valor invalida todos os tokens existentes.
     */
    private int tokenVersion = 1;
}
