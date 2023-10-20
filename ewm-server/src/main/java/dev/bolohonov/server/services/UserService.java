package dev.bolohonov.server.services;

import dev.bolohonov.server.model.User;
import dev.bolohonov.server.dto.user.UserDto;

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
    Optional<User> getUserByName(String name);

    /**
     * Удалить пользователя
     */
    void deleteUser(Long userId);
}
