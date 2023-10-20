package dev.bolohonov.server.dto.compilation;

import dev.bolohonov.server.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class CompilationDto {
    /**
     * уникальный идентификатор
     */
    private Long id;
    /**
     * Заголовок события
     */
    @NotBlank
    private String title;
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;
    /**
     * Список событий, входящих в подборку
     */
    private Set<Event> events;
}
