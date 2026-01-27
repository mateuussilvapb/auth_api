package com.mssousa.auth.domain.model.token;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtPayload Tests")
class JwtPayloadTest {

    private String issuer;
    private String subject;
    private String audience;
    private Instant issuedAt;
    private Instant expiresAt;
    private String jwtId;

    private UserId userId;
    private String username;
    private String email;
    private String name;

    private boolean master;

    private SystemId systemId;
    private List<String> systemRoles;

    private String authMethod;
    private String sessionId;
    private int tokenVersion;

    @BeforeEach
    void setUp() {
        issuer = "https://auth.example.com";
        subject = "user-123";
        audience = "client-system";
        issuedAt = Instant.now();
        expiresAt = issuedAt.plusSeconds(3600);
        jwtId = "jwt-id-123";

        userId = UserId.of(1L);
        username = "testuser";
        email = "test@example.com";
        name = "Test User";

        master = false;

        systemId = SystemId.of(10L);
        systemRoles = List.of("ADMIN", "TEACHER");

        authMethod = "password";
        sessionId = "session-abc";
        tokenVersion = 1;
    }

    // ==================== Criação Válida ====================

    @Nested
    @DisplayName("Valid Payload Creation")
    class ValidPayloadCreation {

        @Test
        @DisplayName("Should create valid JWT payload with all fields")
        void shouldCreateValidJwtPayload() {
            JwtPayload payload = createPayload();

            assertNotNull(payload);
            
            // RFC Claims
            assertEquals(issuer, payload.issuer());
            assertEquals(subject, payload.subject());
            assertEquals(audience, payload.audience());
            assertEquals(issuedAt, payload.issuedAt());
            assertEquals(expiresAt, payload.expiresAt());
            assertEquals(jwtId, payload.jwtId());

            // User Identity
            assertEquals(userId, payload.userId());
            assertEquals(username, payload.username());
            assertEquals(email, payload.email());
            assertEquals(name, payload.name());

            // Master
            assertFalse(payload.isMaster());

            // System Context
            assertEquals(systemId, payload.systemId());
            assertEquals(systemRoles, payload.systemRoles());

            // Security
            assertEquals(authMethod, payload.authMethod());
            assertEquals(sessionId, payload.sessionId());
            assertEquals(tokenVersion, payload.tokenVersion());
        }

        @Test
        @DisplayName("Should create payload for master user without roles")
        void shouldCreatePayloadForMasterUserWithoutRoles() {
            JwtPayload payload = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .master(true)
                    .systemId(systemId)
                    .systemRoles(null)  // Master não precisa de roles
                    .authMethod(authMethod)
                    .sessionId(sessionId)
                    .tokenVersion(tokenVersion)
                    .build();

            assertNotNull(payload);
            assertTrue(payload.isMaster());
            assertNotNull(payload.systemRoles());
            assertTrue(payload.systemRoles().isEmpty());
        }

        @Test
        @DisplayName("Should use default values when not specified")
        void shouldUseDefaultValues() {
            JwtPayload payload = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .systemId(systemId)
                    .systemRoles(systemRoles)
                    .sessionId(sessionId)
                    // Não define authMethod e tokenVersion
                    .build();

            assertEquals("password", payload.authMethod());
            assertEquals(1, payload.tokenVersion());
        }
    }

    // ==================== Validações de Campos Obrigatórios ====================

    @Nested
    @DisplayName("Required Fields Validation")
    class RequiredFieldsValidation {

        @Test
        @DisplayName("Should fail when issuer is null")
        void shouldFailWhenIssuerIsNull() {
            assertValidationFails(
                builder().issuer(null),
                JwtPayload.ERROR_ISSUER_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when subject is null")
        void shouldFailWhenSubjectIsNull() {
            assertValidationFails(
                builder().subject(null),
                JwtPayload.ERROR_SUBJECT_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when audience is null")
        void shouldFailWhenAudienceIsNull() {
            assertValidationFails(
                builder().audience(null),
                JwtPayload.ERROR_AUDIENCE_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when issuedAt is null")
        void shouldFailWhenIssuedAtIsNull() {
            assertValidationFails(
                builder().issuedAt(null),
                JwtPayload.ERROR_ISSUED_AT_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when expiresAt is null")
        void shouldFailWhenExpiresAtIsNull() {
            assertValidationFails(
                builder().expiresAt(null),
                JwtPayload.ERROR_EXPIRES_AT_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when jwtId is null")
        void shouldFailWhenJwtIdIsNull() {
            assertValidationFails(
                builder().jwtId(null),
                JwtPayload.ERROR_JWT_ID_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when userId is null")
        void shouldFailWhenUserIdIsNull() {
            assertValidationFails(
                builder().userId(null),
                JwtPayload.ERROR_USER_ID_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when username is null")
        void shouldFailWhenUsernameIsNull() {
            assertValidationFails(
                builder().username(null),
                JwtPayload.ERROR_USERNAME_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when email is null")
        void shouldFailWhenEmailIsNull() {
            assertValidationFails(
                builder().email(null),
                JwtPayload.ERROR_EMAIL_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when name is null")
        void shouldFailWhenNameIsNull() {
            assertValidationFails(
                builder().name(null),
                JwtPayload.ERROR_NAME_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when systemId is null")
        void shouldFailWhenSystemIdIsNull() {
            assertValidationFails(
                builder().systemId(null),
                JwtPayload.ERROR_SYSTEM_ID_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when authMethod is null")
        void shouldFailWhenAuthMethodIsNull() {
            assertValidationFails(
                builder().authMethod(null),
                JwtPayload.ERROR_AUTH_METHOD_REQUIRED
            );
        }

        @Test
        @DisplayName("Should fail when sessionId is null")
        void shouldFailWhenSessionIdIsNull() {
            assertValidationFails(
                builder().sessionId(null),
                JwtPayload.ERROR_SESSION_ID_REQUIRED
            );
        }
    }

    // ==================== Validações de Regras de Negócio ====================

    @Nested
    @DisplayName("Business Rules Validation")
    class BusinessRulesValidation {

        @Test
        @DisplayName("Should fail when expiration is before issuedAt")
        void shouldFailWhenExpirationIsBeforeIssuedAt() {
            Instant invalidExp = issuedAt.minusSeconds(10);

            assertValidationFails(
                builder().expiresAt(invalidExp),
                JwtPayload.ERROR_INVALID_EXPIRATION
            );
        }

        @Test
        @DisplayName("Should fail when non-master user has null system roles")
        void shouldFailWhenNotMasterAndSystemRolesIsNull() {
            assertValidationFails(
                builder()
                    .master(false)
                    .systemRoles(null),
                JwtPayload.ERROR_SYSTEM_ROLES_REQUIRED
            );
        }

        @Test
        @DisplayName("Should accept empty roles list for master user")
        void shouldAcceptEmptyRolesForMasterUser() {
            JwtPayload payload = builder()
                    .master(true)
                    .systemRoles(List.of())
                    .build();

            assertTrue(payload.isMaster());
            assertTrue(payload.systemRoles().isEmpty());
        }
    }

    // ==================== Builder Behavior ====================

    @Nested
    @DisplayName("Builder Behavior")
    class BuilderBehavior {

        @Test
        @DisplayName("Should allow adding roles individually")
        void shouldAllowAddingRolesIndividually() {
            JwtPayload payload = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .systemId(systemId)
                    .addSystemRole("ADMIN")
                    .addSystemRole("TEACHER")
                    .addSystemRole("COORDINATOR")
                    .sessionId(sessionId)
                    .build();

            assertEquals(3, payload.systemRoles().size());
            assertTrue(payload.systemRoles().contains("ADMIN"));
            assertTrue(payload.systemRoles().contains("TEACHER"));
            assertTrue(payload.systemRoles().contains("COORDINATOR"));
        }

        @Test
        @DisplayName("Should ignore null or blank roles when adding")
        void shouldIgnoreNullOrBlankRoles() {
            JwtPayload payload = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .systemId(systemId)
                    .addSystemRole("ADMIN")
                    .addSystemRole(null)      // Deve ignorar
                    .addSystemRole("")        // Deve ignorar
                    .addSystemRole("   ")     // Deve ignorar
                    .addSystemRole("USER")
                    .sessionId(sessionId)
                    .build();

            assertEquals(2, payload.systemRoles().size());
            assertTrue(payload.systemRoles().contains("ADMIN"));
            assertTrue(payload.systemRoles().contains("USER"));
        }

        @Test
        @DisplayName("Should handle null systemRoles list gracefully")
        void shouldHandleNullSystemRolesListGracefully() {
            JwtPayload.Builder builder = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .systemId(systemId)
                    .sessionId(sessionId)
                    .master(true)
                    .systemRoles(null);

            assertDoesNotThrow(() -> builder.build());
        }
    }

    // ==================== Token Expiration ====================

    @Nested
    @DisplayName("Token Expiration")
    class TokenExpiration {

        @Test
        @DisplayName("Should detect expired token")
        void shouldDetectExpiredToken() {
            JwtPayload payload = createPayload();
            
            Instant afterExpiration = expiresAt.plusSeconds(1);
            
            assertTrue(payload.isExpired(afterExpiration));
            assertFalse(payload.isValid(afterExpiration));
        }

        @Test
        @DisplayName("Should detect valid token")
        void shouldDetectValidToken() {
            JwtPayload payload = createPayload();
            
            Instant beforeExpiration = expiresAt.minusSeconds(1);
            
            assertFalse(payload.isExpired(beforeExpiration));
            assertTrue(payload.isValid(beforeExpiration));
        }

        @Test
        @DisplayName("Should consider token expired at exact expiration time")
        void shouldConsiderTokenExpiredAtExactExpirationTime() {
            JwtPayload payload = createPayload();
            
            // Token expira exatamente em expiresAt
            assertFalse(payload.isExpired(expiresAt));
            assertTrue(payload.isValid(expiresAt));
        }
    }

    // ==================== Imutabilidade ====================

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("System roles list should be immutable")
        void systemRolesShouldBeImmutable() {
            JwtPayload payload = createPayload();

            assertThrows(UnsupportedOperationException.class,
                    () -> payload.systemRoles().add("HACKER"));
        }

        @Test
        @DisplayName("System roles list should be defensive copy")
        void systemRolesShouldBeDefensiveCopy() {
            List<String> originalRoles = List.of("ADMIN");
            
            JwtPayload payload = JwtPayload.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .audience(audience)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .jwtId(jwtId)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .name(name)
                    .systemId(systemId)
                    .systemRoles(originalRoles)
                    .sessionId(sessionId)
                    .build();

            // Lista original não afeta o payload
            assertNotSame(originalRoles, payload.systemRoles());
        }
    }

    // ==================== Igualdade ====================

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("Payloads with same data should be equal")
        void payloadsWithSameDataShouldBeEqual() {
            JwtPayload p1 = createPayload();
            JwtPayload p2 = createPayload();

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("Payloads with different jwtId should not be equal")
        void payloadsWithDifferentJwtIdShouldNotBeEqual() {
            JwtPayload p1 = createPayload();
            JwtPayload p2 = builder().jwtId("different-jti").build();

            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("Payload should equal itself")
        void payloadShouldEqualItself() {
            JwtPayload payload = createPayload();

            assertEquals(payload, payload);
        }

        @Test
        @DisplayName("Payload should not equal null")
        void payloadShouldNotEqualNull() {
            JwtPayload payload = createPayload();

            assertNotEquals(null, payload);
        }

        @Test
        @DisplayName("Payload should not equal different type")
        void payloadShouldNotEqualDifferentType() {
            JwtPayload payload = createPayload();

            assertNotEquals(payload, "string");
        }
    }

    // ==================== ToString ====================

    @Nested
    @DisplayName("ToString")
    class ToStringTest {

        @Test
        @DisplayName("ToString should contain key information")
        void toStringShouldContainKeyInformation() {
            JwtPayload payload = createPayload();
            String str = payload.toString();

            assertTrue(str.contains(subject));
            assertTrue(str.contains(username));
            assertTrue(str.contains("master=" + master));
            assertTrue(str.contains(systemId.toString()));
        }
    }

    // ==================== Helpers ====================

    private JwtPayload createPayload() {
        return builder().build();
    }

    private JwtPayload.Builder builder() {
        return JwtPayload.builder()
                .issuer(issuer)
                .subject(subject)
                .audience(audience)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .jwtId(jwtId)
                .userId(userId)
                .username(username)
                .email(email)
                .name(name)
                .master(master)
                .systemId(systemId)
                .systemRoles(systemRoles)
                .authMethod(authMethod)
                .sessionId(sessionId)
                .tokenVersion(tokenVersion);
    }

    private void assertValidationFails(JwtPayload.Builder builder, String expectedMessage) {
        DomainException ex = assertThrows(DomainException.class, builder::build);
        assertEquals(expectedMessage, ex.getMessage());
    }
}