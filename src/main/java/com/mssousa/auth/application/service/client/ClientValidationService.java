package com.mssousa.auth.application.service.client;

import com.mssousa.auth.domain.model.system.ClientSystem;

public interface ClientValidationService {
    ClientSystem validateActiveClient(String clientId);
}

