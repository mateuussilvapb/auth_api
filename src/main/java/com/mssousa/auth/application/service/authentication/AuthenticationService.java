package com.mssousa.auth.application.service.authentication;

import com.mssousa.auth.application.service.authentication.model.AuthenticatedUser;

public interface AuthenticationService {
    AuthenticatedUser authenticate(String login, String password);
}
