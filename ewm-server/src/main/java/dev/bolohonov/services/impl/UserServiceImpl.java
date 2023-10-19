package dev.bolohonov.services.impl;

import dev.bolohonov.errors.ApiError;
import dev.bolohonov.errors.user.UserNameDuplicateException;
import dev.bolohonov.mappers.UserMapper;
import dev.bolohonov.model.User;
import dev.bolohonov.dto.user.UserDto;
import dev.bolohonov.repository.user.UserRepository;
import dev.bolohonov.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final String ER_OBJ = "пользователь";

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> findAll() {
        return UserMapper.toUserDto(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getUsers(Integer[] ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(this.getPageNumber(from, size), size,
                Sort.by("id").ascending());
        Set<Long> idSet = new HashSet<>();
        for (Integer id : ids) {
            idSet.add(id.longValue());
        }
        Iterable<User> usersPage = userRepository.getUsersByIds(idSet, pageRequest);
        Collection<User> users = new ArrayList<>();
        usersPage.forEach(users::add);
        return UserMapper.toUserDto(users);
    }

    @Override
    public UserDto saveUser(User user) {
        if (!userRepository.findByName(user.getName()).isPresent()) {
            return UserMapper.toUserDto(user);
        } else {
            throw new UserNameDuplicateException("Ошибка при создании пользователя", user.getName());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getUserById(Long userId) {
        return of(UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                        String.format("При выполнении %s не найден %s c id %s",
                                "getUserById", ER_OBJ, userId))
        )));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> getUserByName(String name) {
        return of(UserMapper.toUserDto(userRepository.findByName(name).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                        String.format("При выполнении %s не найден %s c id %s",
                                "getUserName", ER_OBJ, name))
        )));
    };

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (!getUserById(userId).isPresent()) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                    String.format("При выполнении %s не найден %s c id %s",
                            "deleteUser", ER_OBJ, userId));
        }
        userRepository.deleteById(userId);
    }

    private Integer getPageNumber(Integer from, Integer size) {
        return from % size;
    }
}
