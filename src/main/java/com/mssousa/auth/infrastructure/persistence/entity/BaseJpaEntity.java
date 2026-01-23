package com.mssousa.auth.infrastructure.persistence.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseJpaEntity {

    @Id
    private Long id;

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}