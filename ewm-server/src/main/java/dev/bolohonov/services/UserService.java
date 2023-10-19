package dev.bolohonov.services;

import dev.bolohonov.model.User;
import dev.bolohonov.dto.user.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    /**
     * Получить список всех пользователей
     */
    Collection<UserDto> findAll();


    /**
     * Получить список пользователей
     */
    Collection<UserDto> getUsers(Integer[] ids, Integer from, Integer size);

    /**
     * Добавить пользователя
     */
    UserDto saveUser(User user);

    /**
     * Получить пользователя по id
     */
    Optional<UserDto> getUserById(Long userId);

    /**
     * Получить пользователя по email
     */
    Optional<UserDto> getUserByName(String name);

    /**
     * Удалить пользователя
     */
    void deleteUser(Long userId);
}
