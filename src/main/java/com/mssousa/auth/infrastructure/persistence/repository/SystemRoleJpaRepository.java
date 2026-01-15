package com.mssousa.auth.infrastructure.persistence.repository;

import com.mssousa.auth.infrastructure.persistence.entity.SystemRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemRoleJpaRepository extends JpaRepository<SystemRoleEntity, Long> {
    List<SystemRoleEntity> findBySystemId(Long systemId);
    Optional<SystemRoleEntity> findBySystemIdAndCode(Long systemId, String code);
}
