package com.mssousa.auth.application.service.client;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;

import com.mssousa.auth.domain.model.system.ClientSystem;
import com.mssousa.auth.domain.repository.ClientSystemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientValidationServiceImpl implements ClientValidationService {

    private final ClientSystemRepository repository;

    @Override
    public ClientSystem validateActiveClient(String clientId) {

        ClientSystem client = repository.findByClientId(clientId)
                .orElseThrow(() ->
                        new OAuth2AuthenticationException(
                                new OAuth2Error("invalid_client")));

        if (!client.isActive()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_client"));
        }

        return client;
    }
}
