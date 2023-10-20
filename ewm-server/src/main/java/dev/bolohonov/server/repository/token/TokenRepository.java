package dev.bolohonov.server.repository.token;

import dev.bolohonov.server.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
