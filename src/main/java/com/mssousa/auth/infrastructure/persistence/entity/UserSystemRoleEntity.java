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
@Table(name = "user_system_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole}.
 * <p>
 * Mapeia o vínculo entre um acesso de usuário (UserSystem) e um perfil (SystemRole).
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class UserSystemRoleEntity extends AuditableJpaEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_system_id", nullable = false)
    private UserSystemEntity userSystem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "system_role_id", nullable = false)
    private SystemRoleEntity systemRole;

    @Column(nullable = false)
    private String status;
}
