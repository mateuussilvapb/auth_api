package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.binding.userSystem.UserSystemId;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRole;
import com.mssousa.auth.domain.model.binding.userSystemRole.UserSystemRoleId;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import java.util.List;
import java.util.Optional;

public interface UserSystemRoleRepository {
    UserSystemRole save(UserSystemRole userSystemRole);
    Optional<UserSystemRole> findById(UserSystemRoleId id);
    List<UserSystemRole> findByUserSystemId(UserSystemId userSystemId);
    Optional<UserSystemRole> findByUserSystemIdAndSystemRoleId(UserSystemId userSystemId, SystemRoleId systemRoleId);
    void deleteById(UserSystemRoleId id);
}
