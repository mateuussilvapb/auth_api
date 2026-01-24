package com.mssousa.auth.integration.notification;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import com.mssousa.auth.infrastructure.email.SmtpEmailSender;
import com.mssousa.auth.infrastructure.email.config.MailConfig;

@TestConfiguration
@Import({ MailConfig.class, SmtpEmailSender.class })
public class EmailTestConfig {
}
