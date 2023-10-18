package dev.bolohonov.repository.event;

import dev.bolohonov.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    /**
     * Получить список событий по id инициатора
     */
    Page<Event> findEventsByInitiatorId(Long initiatorId, Pageable pageable);
}
