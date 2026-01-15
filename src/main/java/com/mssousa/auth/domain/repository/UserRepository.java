package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.Username;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByUsername(Username username);
    Optional<User> findByEmail(Email email);
    boolean existsByUsername(Username username);
    boolean existsByEmail(Email email);
    void deleteById(UserId id);
}
