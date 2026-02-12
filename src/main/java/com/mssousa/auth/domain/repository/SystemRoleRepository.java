package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.role.SystemRole;
import com.mssousa.auth.domain.model.role.SystemRoleId;
import com.mssousa.auth.domain.model.system.SystemId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SystemRoleRepository {
    SystemRole save(SystemRole role);
    Optional<SystemRole> findById(SystemRoleId id);
    List<SystemRole> findAllById(Set<SystemRoleId> ids);
    List<SystemRole> findBySystemId(SystemId systemId);
    Optional<SystemRole> findBySystemIdAndCode(SystemId systemId, String code);
    void deleteById(SystemRoleId id);
}
