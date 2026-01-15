package com.mssousa.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client_systems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.system.ClientSystem}.
 * <p>
 * Mapeia os dados do sistema cliente para a tabela "client_systems".
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class ClientSystemEntity extends AuditableJpaEntity {

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(nullable = false)
    private String name;

    @Column(name = "redirect_uri", nullable = false)
    private String redirectUri;

    @Column(nullable = false)
    private String status;
}