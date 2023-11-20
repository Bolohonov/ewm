package dev.bolohonov.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    /**
     * Уникальный идентификатор пользователя
     */
    private Long id;
    /**
     * Название категории
     */
    private String name;
}
