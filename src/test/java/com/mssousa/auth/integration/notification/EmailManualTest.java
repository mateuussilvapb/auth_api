package com.mssousa.auth.integration.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mssousa.auth.domain.service.EmailSender;
@SpringBootTest(
    classes = EmailTestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("dev")
@DisplayName("Teste de Integração - Envio de Email SMTP")
class EmailManualTest {

    @Autowired
    private EmailSender emailSender;

    @Test
    void shouldSendRealEmail() {
        emailSender.send(
            "mateuussilvapb@gmail.com",
            "Auth Server - Teste SMTP",
            "Email enviado via teste de integração isolado."
        );
    }
}
