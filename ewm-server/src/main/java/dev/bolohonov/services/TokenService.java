package dev.bolohonov.services;

import dev.bolohonov.model.Token;

import java.util.Optional;

public interface TokenService {

    Token saveToken(Token token);

    Optional<Token> findById(Long id);

}
