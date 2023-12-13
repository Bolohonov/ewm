package dev.bolohonov.server.services.impl.event;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.mappers.EventMapper;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.model.EventState;
import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.repository.event.EventRepository;
import dev.bolohonov.server.services.EventService;
import dev.bolohonov.server.services.EventServiceAdmin;
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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventService eventService;

    @Transactional
    @Override
    public Collection<EventFullDto> findEvents(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.debug("Получен запрос на вывод списка событий администратором");
        Collection<Event> events = eventRepository.getEventsByAdmin(
                eventService.getSetOfParams(users),
                eventService.getSetOfParams(states),
                eventService.getSetOfParams(categories),
                getAndValidateTimeRange(rangeStart, rangeEnd),
                from,
                size
        ).stream().map(eventService::updateEventState).collect(Collectors.toList());
        return eventMapper.toEventFullDto(events);
    }

    @Transactional
    @Override
    public Optional<EventAddDto> updateEvent(Long eventId, EventAddDto newEventDto) {
        log.debug("Получен запрос на обновление события администратором");
        Event oldEvent = eventService.getEventFromRepository(eventId);
        Event newEvent = eventMapper.fromEventUpdateDtoToUpdate(newEventDto, oldEvent, oldEvent.getConfirmedRequests(),
                oldEvent.getCreatedOn(), oldEvent.getInitiatorId(), oldEvent.getPublishedOn(),
                oldEvent.getState(), oldEvent.getViews());
        newEvent.setId(oldEvent.getId());
        return of(eventMapper.toEventAddDto(eventRepository.save(newEvent)));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {
        log.debug("Получен запрос на публикацию события администратором");
        Event event = eventService.getEventFromRepository(eventId);
        validateEventForPublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        return of(eventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public Optional<EventFullDto> rejectEvent(Long eventId) {
        log.debug("Получен запрос на отмену события администратором");
        Event event = eventService.getEventFromRepository(eventId);
        validateEventForRejecting(event);
        event.setState(EventState.CANCELED);
        return of(eventMapper.toEventFullDto(event));
    }

    private Map<String, LocalDateTime> getAndValidateTimeRange(String rangeStart, String rangeEnd) {
        log.debug("Получение временного интервала в eventService");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        if (rangeStart != null) {
            LocalDateTime parsedStart = LocalDateTime.parse(rangeStart, formatter);
            timeMap.put("start", parsedStart);
        }
        if (rangeEnd != null) {
            LocalDateTime parsedEnd = LocalDateTime.parse(rangeEnd, formatter);
            timeMap.put("end", parsedEnd);
        }
        return timeMap;
    }

    private void validateEventForPublishing(Event event) {
        log.debug("Проверка события перед публикацией");
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Поле eventDate указано неверно.",
                    String.format("Error: must be a date in the present or in the future (plus 1 hour). " +
                            "Value: %s", event.getEventDate().toString()));
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", EventState.PENDING));
        }
    }

    private void validateEventForRejecting(Event event) {
        log.debug("Проверка события для отклонения");
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "У события неверный статус",
                    String.format("Error: событие должно иметь статус %s", EventState.PUBLISHED));
        }
    }
}
