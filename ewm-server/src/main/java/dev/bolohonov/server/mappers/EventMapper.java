package dev.bolohonov.server.mappers;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.model.EventState;
import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.dto.event.EventShortDto;
import dev.bolohonov.server.dto.event.EventUpdateDto;
import dev.bolohonov.server.repository.category.CategoryRepository;
import dev.bolohonov.server.repository.feedback.FeedbackRepository;
import dev.bolohonov.server.services.EventService;
import dev.bolohonov.server.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMapper {
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final FeedbackRepository likeRepository;
    private final EventService eventService;

    @Transactional
    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventFullDto.CategoryDto(event.getCategory(),
                        categoryRepository.findById(event.getCategory()).get().getName()),
                categoryRepository.findById(event.getCategory()).get().getName(),
                event.getConfirmedRequests() != null ? event.getConfirmedRequests() : null,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                eventService.calculateEventDuration(event),
                new EventFullDto.UserShortDto(event.getInitiatorId(),
                        userService.getUserById(event.getInitiatorId()).get().getName()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getViews(),
                new EventFullDto.Location(event.getLocLat(), event.getLocLon()),
                likeRepository.getRating(event.getId())
        );
    }

    public Collection<EventFullDto> toEventFullDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventFullDto(e))
                .collect(Collectors.toList());
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new EventShortDto.CategoryDto(event.getCategory(),
                        categoryRepository.findById(event.getCategory()).get().getName()),
                categoryRepository.findById(event.getCategory()).get().getName(),
                event.getConfirmedRequests(),
                event.getParticipantLimit(),
                event.getEventDate(),
                eventService.calculateEventDuration(event),
                userService.getUserById(event.getInitiatorId()).get().getName(),
                event.getPaid(),
                event.getViews(),
                likeRepository.getRating(event.getId()),
                event.getState()
        );
    }

    public Collection<EventShortDto> toEventShortDto(Collection<Event> events) {
        return events.stream()
                .map(e -> toEventShortDto(e))
                .collect(Collectors.toList());
    }

    public Event fromEventAddDto(EventAddDto event, Long userId) throws ApiError {
        log.debug("Получено событие для конвертации");
        if (event.getLocation() == null) {
            throw new ApiError(HttpStatus.BAD_REQUEST,
                    "Ошибка создания события", "Вы не указали локацию");
        }
        return new Event(
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                0L,
                LocalDateTime.now(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventEndDate(),
                userId,
                event.getPaid(),
                event.getParticipantLimit() != null ? event.getParticipantLimit() : 1,
                LocalDateTime.now(),
                event.getRequestModeration() != null ? event.getRequestModeration() : false,
                EventState.PENDING,
                0L,
                event.getLocation() != null ? event.getLocation().getLat() : 0.0,
                event.getLocation() != null ? event.getLocation().getLon() : 0.0,
                0L
        );
    }

    public Event fromEventUpdateDtoToUpdate(EventAddDto newEvent, Event oldEvent, Long confirmedRequests,
                                            LocalDateTime createdOn, Long initiatorId,
                                            LocalDateTime publishedOn, EventState state, Long views) throws ApiError {
        log.debug("Получено событие для конвертации");
        return new Event(
                newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle(),
                newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation(),
                newEvent.getCategory() != null ? newEvent.getCategory() : oldEvent.getCategory(),
                confirmedRequests,
                createdOn,
                newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription(),
                newEvent.getEventDate() != null ? newEvent.getEventDate() : oldEvent.getEventDate(),
                newEvent.getEventEndDate() != null ? newEvent.getEventEndDate() : oldEvent.getEventEndDate(),
                initiatorId,
                newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getPaid(),
                newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit()
                        : oldEvent.getParticipantLimit(),
                publishedOn,
                newEvent.getRequestModeration() != null ? newEvent.getRequestModeration()
                        : oldEvent.getRequestModeration(),
                state,
                views,
                newEvent.getLocation() != null ? newEvent.getLocation().getLat() : oldEvent.getLocLat(),
                newEvent.getLocation() != null ? newEvent.getLocation().getLon() : oldEvent.getLocLon(),
                likeRepository.getRating(oldEvent.getId())
        );
    }

    public Event fromEventUpdateDtoToUpdate(EventUpdateDto newEvent, Event oldEvent, Long confirmedRequests,
                                            LocalDateTime createdOn, Long initiatorId,
                                            LocalDateTime publishedOn, EventState state, Long views) throws ApiError {
        log.debug("Получено событие для конвертации");
        return new Event(
                oldEvent.getId(),
                newEvent.getTitle() != null ? newEvent.getTitle() : oldEvent.getTitle(),
                newEvent.getAnnotation() != null ? newEvent.getAnnotation() : oldEvent.getAnnotation(),
                newEvent.getCategory() != null ? newEvent.getCategory() : oldEvent.getCategory(),
                confirmedRequests,
                createdOn,
                newEvent.getDescription() != null ? newEvent.getDescription() : oldEvent.getDescription(),
                newEvent.getEventDate() != null ? newEvent.getEventDate() : oldEvent.getEventDate(),
                newEvent.getEventEndDate() != null ? newEvent.getEventEndDate() : oldEvent.getEventEndDate(),
                initiatorId,
                newEvent.getPaid() != null ? newEvent.getPaid() : oldEvent.getPaid(),
                newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit()
                        : oldEvent.getParticipantLimit(),
                publishedOn,
                newEvent.getRequestModeration() != null ? newEvent.getRequestModeration()
                        : oldEvent.getRequestModeration(),
                state,
                views,
                newEvent.getLocation() != null ? newEvent.getLocation().getLat() : oldEvent.getLocLat(),
                newEvent.getLocation() != null ? newEvent.getLocation().getLon() : oldEvent.getLocLon(),
                likeRepository.getRating(oldEvent.getId())
        );
    }

    public EventAddDto toEventAddDto(Event event) throws ApiError {
        log.debug("Получено событие для конвертации");
        return new EventAddDto(
                event.getTitle(),
                event.getAnnotation(),
                event.getCategory(),
                event.getDescription(),
                event.getEventDate(),
                event.getEventEndDate(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                new EventAddDto.Location(event.getLocLat(), event.getLocLon())
        );
    }
}
