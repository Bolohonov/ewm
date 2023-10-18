package dev.bolohonov.controllers.privateAPI.event;

import dev.bolohonov.dto.event.EventAddDto;
import dev.bolohonov.dto.event.EventFullDto;
import dev.bolohonov.services.EventServiceAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventServiceAdmin eventService;

    @GetMapping
    @ResponseStatus(OK)
    public Collection<EventFullDto> findEventsByUser(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10")
                                                     Integer size) {
        return eventService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventAddDto> updateEvent(@PathVariable Long eventId,
                                             @RequestBody EventAddDto event) {
        log.debug("Поступил запрос на обновление события администратором");
        return eventService.updateEvent(eventId, event);
    }

    @PatchMapping("/{eventId}/publish")
    @ResponseStatus(OK)
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId) {
        log.debug("Поступил запрос на публикацию события");
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    @ResponseStatus(OK)
    public Optional<EventFullDto> rejectEvent(@PathVariable Long eventId) {
        log.debug("Поступил запрос на отклонение публикации события");
        return eventService.rejectEvent(eventId);
    }
}
