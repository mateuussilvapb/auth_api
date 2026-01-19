package com.mssousa.auth.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mssousa.auth.infrastructure.security.jwt.JwtProperties;

/**
 * Configuração de segurança para JWT.
 * Habilita as propriedades de configuração JWT.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    // Esta classe habilita o carregamento das JwtProperties
    // Configurações adicionais de segurança podem ser adicionadas aqui no futuro
}
