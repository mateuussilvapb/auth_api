package com.mssousa.auth.infrastructure.persistence.repository;

import com.mssousa.auth.infrastructure.persistence.entity.AuthorizationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationCodeJpaRepository extends JpaRepository<AuthorizationCodeEntity, Long> {
    Optional<AuthorizationCodeEntity> findByCode(String code);
}
