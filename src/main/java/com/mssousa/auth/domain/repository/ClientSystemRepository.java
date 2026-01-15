package com.mssousa.auth.domain.repository;

import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.model.system.SystemId;
import java.util.Optional;

public interface ClientSystemRepository {
    ClientSystem save(ClientSystem system);
    Optional<ClientSystem> findById(SystemId id);
    Optional<ClientSystem> findByClientId(String clientId);
    boolean existsByClientId(String clientId);
    void deleteById(SystemId id);
}
