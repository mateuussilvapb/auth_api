package com.mssousa.auth.infrastructure.persistence.entity;

import com.mssousa.auth.infrastructure.persistence.id.TsidGenerator;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class BaseJpaEntity {

    @Id
    private Long id;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            this.id = TsidGenerator.generateLong();
        }
    }

    public Long getId() {
        return id;
    }
}