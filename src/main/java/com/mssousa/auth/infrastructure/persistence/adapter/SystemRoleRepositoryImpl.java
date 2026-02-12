package com.mssousa.auth.infrastructure.persistence.adapter;

import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.repository.SystemRoleRepository;
import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.SystemRoleEntity;
import com.mssousa.auth.infrastructure.persistence.jpa.ClientSystemJpaRepository;
import com.mssousa.auth.infrastructure.persistence.jpa.SystemRoleJpaRepository;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SystemRoleRepositoryImpl implements SystemRoleRepository {

    private final SystemRoleJpaRepository jpaRepository;
    private final ClientSystemJpaRepository clientSystemJpaRepository;
    private final AuthMapper mapper;

    @Override
    public SystemRole save(SystemRole role) {
        // Precisamos buscar a entidade do sistema pai para montar o relacionamento
        ClientSystemEntity systemEntity = clientSystemJpaRepository.getReferenceById(role.getSystemId().value());
        
        SystemRoleEntity entity = mapper.toEntity(role, systemEntity);
        SystemRoleEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<SystemRole> findById(SystemRoleId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<SystemRole> findBySystemId(SystemId systemId) {
        return jpaRepository.findBySystemId(systemId.value()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override 
    public List<SystemRole> findAllById(Set<SystemRoleId> ids) {
        List<Long> idsValues = ids.stream()
                .map(SystemRoleId::value)
                .collect(Collectors.toList());

        return jpaRepository.findAllById(idsValues).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SystemRole> findBySystemIdAndCode(SystemId systemId, String code) {
        return jpaRepository.findBySystemIdAndCode(systemId.value(), code)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(SystemRoleId id) {
        jpaRepository.deleteById(id.value());
    }

}
