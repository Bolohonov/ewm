package dev.bolohonov.server.services.impl.event;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.mappers.EventMapper;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.dto.event.EventShortDto;
import dev.bolohonov.server.repository.event.EventRepository;
import dev.bolohonov.server.services.EventServicePublic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventServiceImpl eventService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort,
                                               Integer from, Integer size) {
        log.debug("Получен запрос на вывод списка событий");
        Collection<Event> events = eventRepository.getEvents(text,
                eventService.getSetOfParams(categories), paid,
                getAndValidateTimeRangeWithDefault(rangeStart, rangeEnd),
                onlyAvailable,
                getSortString(sort), from, size);
        events.stream().forEach(e -> e.addView());
        return eventMapper.toEventShortDto(events);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EventFullDto> getPublishedEventById(Long eventId) {
        log.debug("Получен запрос на вывод списка опубликованных событий");
        Event event = eventService.getEventFromRepository(eventId);
        if (event.getPublishedOn() == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("При выполнении %s не найдено событие c id %s",
                            "getPublishedEventById", eventId));
        }
        event.addView();
        return of(eventMapper.toEventFullDto(event));
    }

    private Map<String, LocalDateTime> getAndValidateTimeRangeWithDefault(String rangeStart, String rangeEnd) {
        log.debug("Получение временного интервала в eventService по умолчанию");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        if (rangeStart != null) {
            LocalDateTime parsedStart = LocalDateTime.parse(rangeStart, formatter);
            timeMap.put("start", parsedStart);
        } else {
            timeMap.put("start", LocalDateTime.now());
        }
        if (rangeEnd != null) {
            LocalDateTime parsedEnd = LocalDateTime.parse(rangeEnd, formatter);
            timeMap.put("end", parsedEnd);
        }
        return timeMap;
    }

    private String getSortString(String sort) {
        String result = "id";
        if (sort != null) {
            result = sort;
        }
        if (sort != null && sort.equals("EVENT_DATE")) {
            result = "eventDate";
        }
        if (sort != null && sort.equals("VIEWS")) {
            result = "views";
        }
        return result;
    }
}
