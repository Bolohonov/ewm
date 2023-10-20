package dev.bolohonov.server.repository.feedback;

public interface FeedbackCountRepository {
    /**
     * Рассчитать рейтинг события по его id
     */
    Long getRating(Long eventId);
}
