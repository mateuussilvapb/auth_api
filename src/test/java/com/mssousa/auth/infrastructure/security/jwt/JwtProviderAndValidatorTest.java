package com.mssousa.auth.infrastructure.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.token.JwtPayload;
import com.mssousa.auth.domain.model.user.UserId;

@DisplayName("JWT Provider and Validator Integration Test")
class JwtProviderAndValidatorTest {

    private JwtProvider jwtProvider;
    private JwtValidator jwtValidator;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        // Configurar propriedades de teste (mínimo 256 bits = 32 caracteres)
        jwtProperties.setSecretKey("test-secret-key-must-be-at-least-256-bits-long-for-hmac-sha");
        jwtProperties.setIssuer("https://test.auth.com");
        jwtProperties.setExpirationMs(3600000L); // 1 hora
        jwtProperties.setTokenVersion(1);

        jwtProvider = new JwtProvider(jwtProperties);
        jwtValidator = new JwtValidator(jwtProperties);
    }

    @Test
    @DisplayName("Deve gerar e validar token JWT com sucesso")
    void shouldGenerateAndValidateJwtSuccessfully() {
        // Arrange
        JwtPayload originalPayload = createTestPayload();

        // Act - Gerar token
        String token = jwtProvider.generateToken(originalPayload);

        // Assert - Token não deve estar vazio
        assertThat(token).isNotBlank();

        // Act - Validar token
        Optional<JwtPayload> extractedPayload = jwtValidator.validateAndExtract(token);

        // Assert - Payload deve ser extraído com sucesso
        assertThat(extractedPayload).isPresent();

        // Assert - Dados do usuário devem ser iguais
        JwtPayload extracted = extractedPayload.get();
        assertThat(extracted.userId()).isEqualTo(originalPayload.userId());
        assertThat(extracted.username()).isEqualTo(originalPayload.username());
        assertThat(extracted.email()).isEqualTo(originalPayload.email());
        assertThat(extracted.name()).isEqualTo(originalPayload.name());
        assertThat(extracted.isMaster()).isEqualTo(originalPayload.isMaster());
        assertThat(extracted.systemId()).isEqualTo(originalPayload.systemId());
        assertThat(extracted.systemRoles()).isEqualTo(originalPayload.systemRoles());
        assertThat(extracted.authMethod()).isEqualTo(originalPayload.authMethod());
        assertThat(extracted.sessionId()).isEqualTo(originalPayload.sessionId());
        assertThat(extracted.tokenVersion()).isEqualTo(originalPayload.tokenVersion());

        // Assert - Claims RFC devem estar presentes
        assertThat(extracted.issuer()).isEqualTo(jwtProperties.getIssuer());
        assertThat(extracted.subject()).isNotBlank();
        assertThat(extracted.audience()).isNotBlank();
        assertThat(extracted.jwtId()).isNotBlank();
        assertThat(extracted.issuedAt()).isNotNull();
        assertThat(extracted.expiresAt()).isNotNull();
        assertThat(extracted.expiresAt()).isAfter(extracted.issuedAt());
    }

    @Test
    @DisplayName("Deve rejeitar token com versão incorreta")
    void shouldRejectTokenWithIncorrectVersion() {
        // Arrange
        JwtPayload payload = createTestPayload();
        String token = jwtProvider.generateToken(payload);

        // Act - Alterar versão do token nas propriedades
        jwtProperties.setTokenVersion(2);

        // Assert - Token deve ser inválido
        Optional<JwtPayload> result = jwtValidator.validateAndExtract(token);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve rejeitar token com issuer incorreto")
    void shouldRejectTokenWithIncorrectIssuer() {
        // Arrange
        JwtPayload payload = createTestPayload();
        String token = jwtProvider.generateToken(payload);

        // Act - Alterar issuer nas propriedades
        jwtProperties.setIssuer("https://different-issuer.com");

        // Assert - Token deve ser inválido
        Optional<JwtPayload> result = jwtValidator.validateAndExtract(token);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve validar token para usuário MASTER sem roles")
    void shouldValidateTokenForMasterUserWithoutRoles() {
        // Arrange
        JwtPayload masterPayload = new JwtPayload(
                jwtProperties.getIssuer(),
                "user123",
                "system456",
                Instant.now(),
                Instant.now().plusMillis(jwtProperties.getExpirationMs()),
                UUID.randomUUID().toString(),
                new UserId(123L),
                "admin",
                "admin@test.com",
                "Admin User",
                true, // master = true
                new SystemId(456L),
                List.of(), // Sem roles (permitido para MASTER)
                "password",
                UUID.randomUUID().toString(),
                jwtProperties.getTokenVersion()
        );

        // Act
        String token = jwtProvider.generateToken(masterPayload);
        Optional<JwtPayload> extracted = jwtValidator.validateAndExtract(token);

        // Assert
        assertThat(extracted).isPresent();
        assertThat(extracted.get().isMaster()).isTrue();
        assertThat(extracted.get().systemRoles()).isEmpty();
    }

    @Test
    @DisplayName("isValid deve retornar true para token válido")
    void shouldReturnTrueForValidToken() {
        // Arrange
        JwtPayload payload = createTestPayload();
        String token = jwtProvider.generateToken(payload);

        // Act & Assert
        assertThat(jwtValidator.isValid(token)).isTrue();
    }

    @Test
    @DisplayName("isValid deve retornar false para token inválido")
    void shouldReturnFalseForInvalidToken() {
        // Act & Assert
        assertThat(jwtValidator.isValid("invalid-token")).isFalse();
    }

    // Helper method para criar payload de teste
    private JwtPayload createTestPayload() {
        return new JwtPayload(
                jwtProperties.getIssuer(),
                "user123",
                "system456",
                Instant.now(),
                Instant.now().plusMillis(jwtProperties.getExpirationMs()),
                UUID.randomUUID().toString(),
                new UserId(123L),
                "testuser",
                "test@example.com",
                "Test User",
                false,
                new SystemId(456L),
                List.of("ROLE_USER", "ROLE_ADMIN"),
                "password",
                UUID.randomUUID().toString(),
                jwtProperties.getTokenVersion()
        );
    }
}
