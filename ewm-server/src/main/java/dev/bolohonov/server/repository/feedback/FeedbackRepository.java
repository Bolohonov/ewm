package dev.bolohonov.server.repository.feedback;

import dev.bolohonov.server.model.Feedback;
import dev.bolohonov.server.model.FeedbackId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId>, FeedbackCountRepository {
    Feedback findByUserIdAndEventId(Long userId, Long eventId);
}
