package dev.bolohonov.repository.token;

import dev.bolohonov.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
