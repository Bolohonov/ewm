package dev.bolohonov.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "feedbacks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FeedbackId.class)
public class Feedback {
    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @Column(name = "user_id")
    private Long userId;
    /**
     * уникальный идентификатор события
     */
    @Id
    @Column(name = "event_id")
    private Long eventId;
    /**
     * флаг like - True, dislike - False
     */
    @Column(name = "is_like")
    private Boolean isLike;
}
