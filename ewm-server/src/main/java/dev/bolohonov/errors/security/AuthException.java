package dev.bolohonov.errors.security;

import dev.bolohonov.errors.ApiError;
import org.springframework.http.HttpStatus;

public class AuthException extends ApiError {
    public AuthException() {
        super(HttpStatus.FORBIDDEN, "Неверный логин или пароль", "Bad credentials");
    }
}
