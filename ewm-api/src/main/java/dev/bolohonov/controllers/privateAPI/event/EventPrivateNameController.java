package dev.bolohonov.controllers.privateAPI.event;

import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.event.EventFullDto;
import dev.bolohonov.server.services.EventServicePrivate;
import dev.bolohonov.server.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/${ewm.api.version}/users/{userName}/events")
@CrossOrigin(origins = {"${ewm.origin.localhost}"},
        allowedHeaders = {"Origin", "Authorization", "X-Requested-With", "Content-Type", "Accept", "Cookie"},
        allowCredentials = "true",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET},
        maxAge = 3600)
public class EventPrivateNameController {
    private final EventServicePrivate eventService;
    private final RequestService requestService;

    @GetMapping("/{eventId}/initiator")
    @ResponseStatus(OK)
    public Boolean isInitiator(@PathVariable String userName, @PathVariable Long eventId) {
        return eventService.isInitiator(userName, eventId);
    }

    @PostMapping("/add")
    @ResponseStatus(OK)
    public Optional<EventFullDto> add(@PathVariable String userName, @RequestBody EventAddDto event) {
        log.debug("Поступил запрос на сохранение");
        return eventService.addEvent(userName, event);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    public Collection<EventFullDto> findEventsByUser(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "500")
                                                     Integer size) {
        log.debug("Поступил запрос на поиск");
        return eventService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

//    @PostMapping("/add")
//    @ResponseStatus(OK)
//    public Optional<EventFullDto> add(@PathVariable String userName, @RequestBody EventAddDto event) {
//        log.debug("Поступил запрос на сохранение");
//        return eventService.addEvent(userName, event);
//    }
}
