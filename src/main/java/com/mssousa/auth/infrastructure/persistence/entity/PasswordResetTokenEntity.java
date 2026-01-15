package com.mssousa.auth.infrastructure.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetToken}.
 * <p>
 * Mapeia os tokens de recuperação de senha para a tabela "password_reset_tokens".
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class PasswordResetTokenEntity extends AuditableJpaEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used;
}
