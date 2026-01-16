package com.mssousa.auth.infrastructure.persistence.repository.adapter;

import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCode;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCodeId;
import com.mssousa.auth.domain.repository.AuthorizationCodeRepository;
import com.mssousa.auth.infrastructure.persistence.entity.AuthorizationCodeEntity;
import com.mssousa.auth.infrastructure.persistence.entity.ClientSystemEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserEntity;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;
import com.mssousa.auth.infrastructure.persistence.repository.AuthorizationCodeJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.ClientSystemJpaRepository;
import com.mssousa.auth.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationCodeRepositoryImpl implements AuthorizationCodeRepository {

    private final AuthorizationCodeJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ClientSystemJpaRepository clientSystemJpaRepository;
    private final AuthMapper mapper;

    @Override
    public AuthorizationCode save(AuthorizationCode authorizationCode) {
        UserEntity userEntity = userJpaRepository.getReferenceById(authorizationCode.getUser().getId().value());
        ClientSystemEntity systemEntity = clientSystemJpaRepository.getReferenceById(authorizationCode.getSystemId().value());

        AuthorizationCodeEntity entity = mapper.toEntity(authorizationCode, userEntity, systemEntity);
        AuthorizationCodeEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AuthorizationCode> findById(AuthorizationCodeId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<AuthorizationCode> findByCode(String code) {
        return jpaRepository.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(AuthorizationCodeId id) {
        jpaRepository.deleteById(id.value());
    }
}
