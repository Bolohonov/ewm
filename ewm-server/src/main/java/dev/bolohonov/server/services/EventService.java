package dev.bolohonov.server.services;

import dev.bolohonov.server.model.Event;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public interface EventService {
    /**
     * Получить событие из репозитория
     */
    Event getEventFromRepository(Long eventId);

    /**
     * Проверить и обновить статус события на CANCELED, если завершено
     */
    Event updateEventState(Event event);

    /**
     * Рассчитать продолжительность события
     */
    Duration calculateEventDuration(Event event);

    /**
     * Получить Set с параметрами
     */
    <T> Set<T> getSetOfParams(List<T> list);
}
