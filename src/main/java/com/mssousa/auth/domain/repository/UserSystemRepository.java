package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.binding.userSystem.UserSystem;
import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;
import java.util.List;
import java.util.Optional;

public interface UserSystemRepository {
    UserSystem save(UserSystem userSystem);
    Optional<UserSystem> findById(UserSystemId id);
    Optional<UserSystem> findByUserIdAndSystemId(UserId userId, SystemId systemId);
    List<UserSystem> findByUserId(UserId userId);
    void deleteById(UserSystemId id);
}
