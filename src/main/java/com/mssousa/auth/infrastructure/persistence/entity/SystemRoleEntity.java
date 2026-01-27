package com.mssousa.auth.infrastructure.persistence.entity;

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
@Table(name = "system_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.role.SystemRole}.
 * <p>
 * Mapeia os dados do perfil de sistema para a tabela "system_roles".
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class SystemRoleEntity extends AuditableJpaEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "system_id", nullable = false)
    private ClientSystemEntity system;

    @Column(nullable = false, length = 50)
    private String code;

    @Column
    private String description;

    @Column(nullable = false)
    private String status;
}
