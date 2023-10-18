package dev.bolohonov;

import dev.bolohonov.model.User;
import dev.bolohonov.dto.user.UserDto;
import dev.bolohonov.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Order(1)
public class Initializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = Logger.getLogger("Initializer");

    @Autowired
    public Initializer(UserService userService, PasswordEncoder passwordEncoder
                       ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        logger.setLevel(Level.ALL);
    }

    @Override
    public void run(String... args) throws Exception {
        Collection<UserDto> users = userService.findAll();
        if (users == null || users.size() == 0) {
            logger.info("Users are empty, creating the default one!");
            createDefaultUser();
        } else {
            logger.info("Users found:");
        }
    }

    private void createDefaultUser() {
        User user = User.builder()
                .name("admin")
                .email("admin@ewm.ru")
                .password_hash(passwordEncoder.encode("mrrHHaUNATsB="))
                .build();

        userService.saveUser(user);
    }
}
