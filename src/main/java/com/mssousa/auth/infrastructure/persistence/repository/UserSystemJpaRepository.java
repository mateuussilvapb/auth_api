package com.mssousa.auth.infrastructure.persistence.repository;

import com.mssousa.auth.infrastructure.persistence.entity.UserSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSystemJpaRepository extends JpaRepository<UserSystemEntity, Long> {
    Optional<UserSystemEntity> findByUserIdAndSystemId(Long userId, Long systemId);
    List<UserSystemEntity> findByUserId(Long userId);
}
