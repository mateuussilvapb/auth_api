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
@Table(name = "authorization_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCode}.
 * <p>
 * Mapeia os dados do código de autorização para a tabela "authorization_codes".
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class AuthorizationCodeEntity extends AuditableJpaEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "system_id", nullable = false)
    private ClientSystemEntity system;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used;
}
