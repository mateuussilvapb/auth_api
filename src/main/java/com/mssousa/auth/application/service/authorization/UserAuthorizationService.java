package com.mssousa.auth.application.service.authorization;

import java.util.Set;

import com.mssousa.auth.domain.model.system.SystemId;
import com.mssousa.auth.domain.model.user.UserId;

public interface UserAuthorizationService {

    AuthorizedUser authorize(
            UserId userId,
            SystemId systemId,
            boolean isMaster);

    record AuthorizedUser(Set<String> roles) {
    }
}
