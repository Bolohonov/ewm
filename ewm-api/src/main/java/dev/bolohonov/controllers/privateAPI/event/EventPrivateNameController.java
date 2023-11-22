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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
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
        return eventService.addEvent(userName, event);
    }

}
