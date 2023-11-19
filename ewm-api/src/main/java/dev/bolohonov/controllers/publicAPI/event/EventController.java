package dev.bolohonov.controllers.publicAPI.event;

import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.dto.event.EventShortDto;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.repository.event.EventRepository;
import dev.bolohonov.server.services.client.StatisticsClient;
import dev.bolohonov.server.services.EventServicePublic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/${ewm.api.version}/events")
@CrossOrigin(origins = {"${ewm.origin.localhost}"},
        allowedHeaders = {"Origin", "Authorization", "X-Requested-With", "Content-Type", "Accept", "Cookie"},
        allowCredentials = "true",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET},
        maxAge = 3600)
public class EventController {
    private final EventServicePublic eventService;
    private final EventRepository eventRepository;
//    private final StatisticsClient statisticsClient;

    @GetMapping("/all")
    @ResponseStatus(OK)
    public Collection<EventShortDto> findEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {
//        statisticsClient.addEndpointHit(request.getRemoteAddr(), request.getRequestURI());
        log.debug("Запрос на получение всех событий в паблик контроллер");
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(OK)
    public Optional<EventFullDto> findEventById(@PathVariable Long eventId,
                                                HttpServletRequest request) {
//        statisticsClient.addEndpointHit(request.getRemoteAddr(), request.getRequestURI());
        log.debug("Запрос на получение события с id %s", eventId);
        return eventService.getPublishedEventById(eventId);
    }

    @GetMapping("/all2")
    @ResponseStatus(OK)
    public Collection<Event> findEvents() {
//        statisticsClient.addEndpointHit(request.getRemoteAddr(), request.getRequestURI());
        log.debug("Запрос на получение всхе событий в паблик контроллер");
        return eventRepository.findAll();
    }
}
