package dev.bolohonov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    /**
     * уникальный идентификатор пользователя
     */
    private Long id;
    /**
     * имя или логин пользователя
     */
    private String name;
    /**
     * адрес электронной почты
     */
    private String email;
}