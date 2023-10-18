package dev.bolohonov.services.impl;

import dev.bolohonov.model.Token;
import dev.bolohonov.repository.token.TokenRepository;
import dev.bolohonov.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Token> findById(Long id) {
        return tokenRepository.findById(id);
    }
}
