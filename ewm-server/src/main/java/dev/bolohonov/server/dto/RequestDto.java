package dev.bolohonov.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.bolohonov.server.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestDto {
    /**
     * уникальный идентификатор заявки
     */
    private Long id;
    /**
     * Дата и время создания заявки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    /**
     * Идентификатор события
     */
    private Long event;
    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requester;
    /**
     * Статус заявки
     */
    private RequestStatus status;
}
