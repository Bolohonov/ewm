package dev.bolohonov.security.services;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.model.User;
import dev.bolohonov.server.repository.user.UserRepository;
import dev.bolohonov.security.UserDetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsHelper detailsHelper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) {
        User user = userRepository.findByName(name).orElseThrow(
                () -> new ApiError(HttpStatus.NOT_FOUND, "Пользователь не найден",
                        String.format("При выполнении %s не найден %s c именем %s",
                                "аутентификации", "пользователь", name))
        );

        return detailsHelper.buildDetails(user);
    }
}
