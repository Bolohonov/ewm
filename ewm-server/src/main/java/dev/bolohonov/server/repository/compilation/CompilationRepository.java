package dev.bolohonov.server.repository.compilation;

import dev.bolohonov.server.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    /**
     * Найти подборки прикрепленные к главной странице
     */
    Page<Compilation> findCompilationsByPinned(Boolean pinned, Pageable pageable);
}
