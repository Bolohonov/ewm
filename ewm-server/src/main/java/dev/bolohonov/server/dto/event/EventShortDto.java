package dev.bolohonov.server.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.bolohonov.server.model.EventState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventShortDto {
    /**
     * уникальный идентификатор
     */
    private Long id;
    /**
     * Заголовок события
     */
    private String title;
    /**
     * Краткое описание
     */
    private String annotation;
    /**
     * Категория
     */
    private CategoryDto category;
    /**
     * Название категории
     */
    private String categoryName;
    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Long confirmedRequests;
    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private Integer participantLimit;
    /**
     * Дата и время на которые намечено событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_GB")
    private LocalDateTime eventDate;
    /**
     * Продолжительность события
     */
    private Duration duration;
    /**
     * Инициатор события
     */
    private String initiatorName;
    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;
    /**
     * Количество просмотрев события
     */
    private Long views;
    /**
     * Рейтинг события
     */
    private Long rating;
    /**
     * Список состояний жизненного цикла события
     */
    private EventState state;

    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class CategoryDto implements Serializable {
        /**
         * уникальный идентификатор
         */
        private Long id;
        /**
         * название категории
         */
        private String name;
    }
}
