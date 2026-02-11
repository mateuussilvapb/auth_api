package com.mssousa.auth.application.service.authentication.model;

import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.Username;

public record AuthenticatedUser(
    UserId userId,
    Username username,
    Email email,
    String name,
    boolean master
) {}