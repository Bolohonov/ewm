package dev.bolohonov.services;

import dev.bolohonov.model.Event;

import java.util.List;
import java.util.Set;

public interface EventService {
    /**
     * Получить событие из репозитория
     */
    Event getEventFromRepository(Long eventId);

    /**
     * Получить Set с параметрами
     */
    <T> Set<T> getSetOfParams(List<T> list);
}
