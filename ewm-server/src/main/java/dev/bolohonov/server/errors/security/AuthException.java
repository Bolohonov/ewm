package dev.bolohonov.server.errors.security;

import dev.bolohonov.server.errors.ApiError;
import org.springframework.http.HttpStatus;

public class AuthException extends ApiError {
    public AuthException(String name) {
        super(HttpStatus.FORBIDDEN, String.format("Неверный логин или пароль пользователя с именем %s", name),
                "Bad credentials");
    }
}
