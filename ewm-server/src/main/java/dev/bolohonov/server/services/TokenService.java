package dev.bolohonov.server.services;

import dev.bolohonov.server.model.Token;

import java.util.Optional;

public interface TokenService {

    Token saveToken(Token token);

    Optional<Token> findById(Long id);

}
