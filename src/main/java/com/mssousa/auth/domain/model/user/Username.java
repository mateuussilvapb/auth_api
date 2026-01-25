package com.mssousa.auth.domain.model.user;

import com.mssousa.auth.domain.exception.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representando o nome de usuário.
 * Garante que o username seja válido conforme regras de negócio.
 */
public final class Username {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    private final String value;

    private Username(String value) {
        validate(value);
        this.value = value;
    }

    public static Username of(String value) {
        return new Username(value);
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException("Username não pode ser nulo ou vazio");
        }

        if (value.length() < MIN_LENGTH) {
            throw new DomainException("Username deve ter pelo menos " + MIN_LENGTH + " caracteres");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Username não pode exceder " + MAX_LENGTH + " caracteres");
        }

        if (!VALID_PATTERN.matcher(value).matches()) {
            throw new DomainException("Username deve conter apenas caracteres alfanuméricos e sublinhados");
        }
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return value.equals(username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
