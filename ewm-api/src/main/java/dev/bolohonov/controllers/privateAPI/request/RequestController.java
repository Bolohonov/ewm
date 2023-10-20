package dev.bolohonov.controllers.privateAPI.request;

import dev.bolohonov.server.dto.RequestDto;
import dev.bolohonov.server.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    @ResponseStatus(OK)
    public Collection<RequestDto> findUserRequests(@PathVariable Long userId) {
        log.debug("Получен запрос в контроллер на поиск запросов пользователя на участие в событиях");
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(OK)
    public Optional<RequestDto> addRequest(@PathVariable Long userId,
                                           @RequestParam Long eventId) {
        log.debug("Получен запрос в контроллер на добавление запроса на участие в событии");
        return requestService.addNewRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(OK)
    public Optional<RequestDto> cancelRequest(@PathVariable Long userId,
                                              @PathVariable Long requestId) {
        log.debug("Получен запрос в контроллер на отмену запроса на участие в событии");
        return requestService.revokeRequest(userId, requestId);
    }
}
