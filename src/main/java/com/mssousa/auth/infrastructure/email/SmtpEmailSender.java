package com.mssousa.auth.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mssousa.auth.domain.service.EmailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${auth.email.sender}")
    private String sender;

    @Override
    public void send(String to, String subject, String body) {
        log.info("Enviando email para {}", to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Erro ao enviar email para {}", to, e);
            throw new IllegalStateException("Falha ao enviar email", e);
        }
    }
}
