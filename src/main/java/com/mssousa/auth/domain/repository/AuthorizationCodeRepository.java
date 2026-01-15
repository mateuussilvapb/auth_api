package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCode;
import com.mssousa.auth.domain.model.token.authorizationCode.AuthorizationCodeId;
import java.util.Optional;

public interface AuthorizationCodeRepository {
    AuthorizationCode save(AuthorizationCode authorizationCode);
    Optional<AuthorizationCode> findById(AuthorizationCodeId id);
    Optional<AuthorizationCode> findByCode(String code);
    void deleteById(AuthorizationCodeId id);
}
