package com.mssousa.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Representação de Persistência do Agregado {@link com.mssousa.auth.domain.model.user.User}.
 * <p>
 * Mapeia os dados do usuário para a tabela "users".
 * Herda auditoria (created_at, created_by) e ID (TSID) de {@link AuditableJpaEntity}.
 * </p>
 */
public class UserEntity extends AuditableJpaEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_master", nullable = false)
    private boolean master;

    @Column(nullable = false)
    private String status;
}
