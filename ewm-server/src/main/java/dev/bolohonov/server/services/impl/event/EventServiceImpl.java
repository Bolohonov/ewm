package dev.bolohonov.server.services.impl.event;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.repository.event.EventRepository;
import dev.bolohonov.server.repository.feedback.FeedbackRepository;
import dev.bolohonov.server.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final FeedbackRepository likeRepository;

    @Override
    @Transactional
    public Event getEventFromRepository(Long eventId) {
        log.debug("Получить событие с id {} из репозитория", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new ApiError(HttpStatus.NOT_FOUND, "Событие не найдено",
                    String.format("Не найдено событие c id %s", eventId
                    ));
        });
        Long rating = likeRepository.getRating(eventId);
        event.setRating(rating);
        return event;
    }

    @Override
    @Transactional
    public <T> Set<T> getSetOfParams(List<T> list) {
        Set<T> set = new HashSet<>();
        if (list != null) {
            set.addAll(list);
        }
        return set;
    }
}
