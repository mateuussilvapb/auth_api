package com.mssousa.auth.infrastructure.persistence.repository;

import com.mssousa.auth.infrastructure.persistence.entity.UserSystemRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSystemRoleJpaRepository extends JpaRepository<UserSystemRoleEntity, Long> {
    List<UserSystemRoleEntity> findByUserSystemId(Long userSystemId);
    Optional<UserSystemRoleEntity> findByUserSystemIdAndSystemRoleId(Long userSystemId, Long systemRoleId);
}
