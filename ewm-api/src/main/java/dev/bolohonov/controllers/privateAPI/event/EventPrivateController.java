package dev.bolohonov.controllers.privateAPI.event;

import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.dto.event.EventShortDto;
import dev.bolohonov.server.dto.event.EventUpdateDto;
import dev.bolohonov.server.dto.RequestDto;
import dev.bolohonov.server.dto.user.UserWithRatingDto;
import dev.bolohonov.server.services.EventServicePrivate;
import dev.bolohonov.server.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventServicePrivate eventService;
    private final RequestService requestService;

    @GetMapping()
    @ResponseStatus(OK)
    public Collection<EventShortDto> findEventsByUser(@PathVariable Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                      Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10")
                                                      Integer size) {
        return eventService.findEventsByInitiator(userId, from, size);
    }

    @PatchMapping()
    @ResponseStatus(OK)
    public Optional<EventFullDto> patchEventByInitiator(@PathVariable Long userId,
                                                        @RequestBody EventUpdateDto event) {
        return eventService.updateEventByInitiator(userId, event);
    }

    @PostMapping()
    @ResponseStatus(OK)
    public Optional<EventFullDto> addNewEvent(@PathVariable Long userId,
                                              @RequestBody EventAddDto event) {
        return eventService.addEvent(userId, event);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventFullDto> findEventById(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventFullDto> changeEventStateToCanceled(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return eventService.changeEventStateToCanceled(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(OK)
    public Collection<RequestDto> findRequestsOfEventInitiator(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        return requestService.getRequestsOfEventInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    @ResponseStatus(OK)
    public Optional<RequestDto> confirmRequest(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @PathVariable Long reqId) {
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    @ResponseStatus(OK)
    public Optional<RequestDto> rejectRequest(@PathVariable Long userId,
                                              @PathVariable Long reqId) {
        return requestService.rejectRequest(userId, reqId);
    }

    @PostMapping("/{eventId}/like")
    @ResponseStatus(OK)
    public Optional<EventFullDto> likeEvent(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("Получен запрос в контроллер на добавление лайка события с id {}", eventId);
        return eventService.addLike(userId, eventId);
    }

    @PostMapping("/{eventId}/dislike")
    @ResponseStatus(OK)
    public Optional<EventFullDto> dislikeEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.debug("Получен запрос в контроллер на добавление дизлайка события с id {}", eventId);
        return eventService.addDislike(userId, eventId);
    }

    @GetMapping("/rating")
    @ResponseStatus(OK)
    public Collection<UserWithRatingDto> getUsersByEventsRating(@PathVariable Long userId,
                                                                @PositiveOrZero @RequestParam(defaultValue = "0")
                                                                    Integer userFrom,
                                                                @Positive @RequestParam(defaultValue = "10")
                                                                    Integer size) {
        log.debug("Получен запрос в контроллер на получение рейтинга пользователей");
        return eventService.getUsersByRating(userFrom, size);
    }
}
