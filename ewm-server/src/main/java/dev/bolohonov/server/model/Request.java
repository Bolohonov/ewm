package dev.bolohonov.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class Request {
    @Builder
    public Request(LocalDateTime created, Long event, Long requester, RequestStatus status) {
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }

    /**
     * уникальный идентификатор заявки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Дата и время создания заявки
     */
    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    /**
     * Идентификатор события
     */
    @Column(name = "event_id")
    private Long event;
    /**
     * Идентификатор пользователя, отправившего заявку
     */
    @Column(name = "requester_id")
    private Long requester;
    /**
     * Статус заявки
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
}
