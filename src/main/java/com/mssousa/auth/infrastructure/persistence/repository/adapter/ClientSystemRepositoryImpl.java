package com.mssousa.auth.infrastructure.persistence.repository.adapter;

import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.repository.ClientSystemRepository;
import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;
import com.mssousa.auth.infrastructure.persistence.repository.ClientSystemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientSystemRepositoryImpl implements ClientSystemRepository {

    private final ClientSystemJpaRepository jpaRepository;
    private final AuthMapper mapper;

    @Override
    public ClientSystem save(ClientSystem system) {
        ClientSystemEntity entity = mapper.toEntity(system);
        ClientSystemEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ClientSystem> findById(SystemId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ClientSystem> findByClientId(String clientId) {
        return jpaRepository.findByClientId(clientId)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByClientId(String clientId) {
        return jpaRepository.existsByClientId(clientId);
    }

    @Override
    public void deleteById(SystemId id) {
        jpaRepository.deleteById(id.value());
    }
}
