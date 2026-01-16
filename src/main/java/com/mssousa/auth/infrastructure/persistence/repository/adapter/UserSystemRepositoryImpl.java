package com.mssousa.auth.infrastructure.persistence.repository.adapter;

import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.repository.UserSystemRepository;
import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserSystemEntity;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;
import com.mssousa.auth.infrastructure.persistence.repository.ClientSystemJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.UserJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.UserSystemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserSystemRepositoryImpl implements UserSystemRepository {

    private final UserSystemJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ClientSystemJpaRepository clientSystemJpaRepository;
    private final AuthMapper mapper;

    @Override
    public UserSystem save(UserSystem userSystem) {
        UserEntity userEntity = userJpaRepository.getReferenceById(userSystem.getUserId().value());
        ClientSystemEntity systemEntity = clientSystemJpaRepository.getReferenceById(userSystem.getSystemId().value());

        UserSystemEntity entity = mapper.toEntity(userSystem, userEntity, systemEntity);
        UserSystemEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserSystem> findById(UserSystemId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<UserSystem> findByUserIdAndSystemId(UserId userId, SystemId systemId) {
        return jpaRepository.findByUserIdAndSystemId(userId.value(), systemId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<UserSystem> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UserSystemId id) {
        jpaRepository.deleteById(id.value());
    }
}
