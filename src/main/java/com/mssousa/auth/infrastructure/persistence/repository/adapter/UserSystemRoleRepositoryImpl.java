package com.mssousa.auth.infrastructure.persistence.repository.adapter;

import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRoleId;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.repository.UserSystemRoleRepository;
import com.mssousa.auth.infrastructure.persistence.entity.SystemRoleEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserSystemRoleEntity;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;
import com.mssousa.auth.infrastructure.persistence.repository.SystemRoleJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.UserSystemJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.UserSystemRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserSystemRoleRepositoryImpl implements UserSystemRoleRepository {

    private final UserSystemRoleJpaRepository jpaRepository;
    private final UserSystemJpaRepository userSystemJpaRepository;
    private final SystemRoleJpaRepository systemRoleJpaRepository;
    private final AuthMapper mapper;

    @Override
    public UserSystemRole save(UserSystemRole userSystemRole) {
        UserSystemEntity userSystemEntity = userSystemJpaRepository.getReferenceById(userSystemRole.getUserSystemId().value());
        SystemRoleEntity systemRoleEntity = systemRoleJpaRepository.getReferenceById(userSystemRole.getSystemRoleId().value());

        UserSystemRoleEntity entity = mapper.toEntity(userSystemRole, userSystemEntity, systemRoleEntity);
        UserSystemRoleEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserSystemRole> findById(UserSystemRoleId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<UserSystemRole> findByUserSystemId(UserSystemId userSystemId) {
        return jpaRepository.findByUserSystemId(userSystemId.value()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserSystemRole> findByUserSystemIdAndSystemRoleId(UserSystemId userSystemId, SystemRoleId systemRoleId) {
        return jpaRepository.findByUserSystemIdAndSystemRoleId(userSystemId.value(), systemRoleId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UserSystemRoleId id) {
        jpaRepository.deleteById(id.value());
    }
}
