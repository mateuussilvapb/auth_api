package com.mssousa.auth.infrastructure.persistence.adapter;

import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetToken;
import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetTokenId;
import com.mssousa.auth.domain.model.token.passwordResetToken.ResetTokenValue;
import com.mssousa.auth.domain.repository.PasswordResetTokenRepository;
import com.mssousa.auth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.mssousa.auth.infrastructure.persistence.entity.UserEntity;
import com.mssousa.auth.infrastructure.persistence.jpa.PasswordResetTokenJpaRepository;
import com.mssousa.auth.infrastructure.persistence.jpa.UserJpaRepository;
import com.mssousa.auth.infrastructure.persistence.mapper.AuthMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AuthMapper mapper;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        UserEntity userEntity = userJpaRepository.getReferenceById(token.getUserId().value());

        PasswordResetTokenEntity entity = mapper.toEntity(token, userEntity);
        PasswordResetTokenEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PasswordResetToken> findById(PasswordResetTokenId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<PasswordResetToken> findByValue(ResetTokenValue value) {
        return jpaRepository.findByToken(value.value())
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(PasswordResetTokenId id) {
        jpaRepository.deleteById(id.value());
    }
}
