package dev.bolohonov.repository.feedback;

import dev.bolohonov.model.Feedback;
import dev.bolohonov.model.FeedbackId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, FeedbackId>, FeedbackCountRepository {
    Feedback findByUserIdAndEventId(Long userId, Long eventId);
}
