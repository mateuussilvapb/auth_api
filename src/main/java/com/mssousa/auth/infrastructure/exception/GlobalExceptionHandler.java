package com.mssousa.auth.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mssousa.auth.domain.exception.DomainException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Exceções de Domínio (Regras de Negócio)
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        log.warn("Domain exception: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.valueOf(422),
                "DomainException",
                ex.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.valueOf(422));
    }

    // Argumentos Inválidos (ex: validações de Value Objects)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "IllegalArgument",
                ex.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Erros de Autenticação (credenciais inválidas)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Authentication failed: Bad credentials");

        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "BadCredentials",
                "Credenciais inválidas",
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Erros de Autenticação Genéricos
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "AuthenticationError",
                "Falha na autenticação",
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Erros de Autorização (acesso negado)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                "AccessDenied",
                "Acesso negado",
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Erros de validação em DTOs (@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error on request body");

        StringBuilder errors = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("\n");
        });

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST,
                "ValidationError", errors.toString(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    // Erros de validação em parâmetros diretos (ex: @RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation on request parameters");

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            String field = cv.getPropertyPath().toString();
            errors.put(field, cv.getMessage());
        });

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "ValidationError",
                "Existem erros de validação nos parâmetros", LocalDateTime.now(), errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Fallback para exceções não tratadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getClass().getSimpleName(),
                "Erro interno do servidor",
                LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Record genérico de mensagem de erro
    public record ErrorResponse(HttpStatus status, String error, String message,
            LocalDateTime timestamp, Map<String, String> custom) {
        public ErrorResponse(HttpStatus status, String error, String message, LocalDateTime timestamp) {
            this(status, error, message, timestamp, Collections.emptyMap());
        }
    }
}