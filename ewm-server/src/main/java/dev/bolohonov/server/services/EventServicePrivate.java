package dev.bolohonov.server.services;

import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.dto.event.EventShortDto;
import dev.bolohonov.server.dto.event.EventUpdateDto;
import dev.bolohonov.server.dto.user.UserWithRatingDto;

import java.util.Collection;
import java.util.Optional;

public interface EventServicePrivate {
    /**
     * Получить событие по id
     */
    Optional<EventFullDto> getEventById(Long eventId);

    /**+
     * Получить список событий по id инициатора
     */
    Collection<EventShortDto> findEventsByInitiator(Long userId, Integer from, Integer size);

    /**+
     * Проверить является ли пользователь инициатором события
     */
    boolean isInitiator(String username, Long eventId);

    /**
     * Инициатор обновляет событие
     */
    default Optional<EventFullDto> updateEventByInitiator(Long userId, EventUpdateDto event) {
        return null;
    }

    /**
     * Добавить событие
     */
    Optional<EventFullDto> addEvent(Long userId, EventAddDto event);

    /**
     * Отменить событие - статус Canceled
     */
    Optional<EventFullDto> changeEventStateToCanceled(Long userId, Long eventId);

    /**
     * Лайкнуть событие
     */
    Optional<EventFullDto> addLike(Long userId, Long eventId);

    /**
     * Дизлайкнуть событие
     */
    Optional<EventFullDto> addDislike(Long userId, Long eventId);

    /**
     * Получение списка инициаторов событий по их рейтингу
     */
    Collection<UserWithRatingDto> getUsersByRating(Integer from, Integer size);
}
