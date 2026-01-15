package com.mssousa.auth.infrastructure.persistence.repository;

import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientSystemJpaRepository extends JpaRepository<ClientSystemEntity, Long> {
    Optional<ClientSystemEntity> findByClientId(String clientId);
    boolean existsByClientId(String clientId);
}
