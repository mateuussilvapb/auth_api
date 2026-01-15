package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetToken;
import com.mssousa.auth.domain.model.token.passwordResetToken.PasswordResetTokenId;
import com.mssousa.auth.domain.model.token.passwordResetToken.ResetTokenValue;
import java.util.Optional;

public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findById(PasswordResetTokenId id);
    Optional<PasswordResetToken> findByValue(ResetTokenValue value);
    void deleteById(PasswordResetTokenId id);
}
