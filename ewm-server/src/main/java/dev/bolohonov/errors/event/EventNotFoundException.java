package dev.bolohonov.errors.event;

import dev.bolohonov.errors.ApiError;
import org.springframework.http.HttpStatus;

public class EventNotFoundException extends ApiError {
    public EventNotFoundException(String reason, Long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Событие с id %s не найдено", id), reason);
    }
}
