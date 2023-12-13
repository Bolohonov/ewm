package dev.bolohonov;

import dev.bolohonov.server.dto.CategoryDto;
import dev.bolohonov.server.dto.event.EventAddDto;
import dev.bolohonov.server.dto.user.UserDto;
import dev.bolohonov.server.model.Category;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.model.User;
import dev.bolohonov.server.repository.event.EventRepository;
import dev.bolohonov.server.services.CategoryService;
import dev.bolohonov.server.services.EventServiceAdmin;
import dev.bolohonov.server.services.EventServicePrivate;
import dev.bolohonov.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Order(1)
public class Initializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventServicePrivate eventServicePrivate;

    private final EventServiceAdmin eventServiceAdmin;

    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = Logger.getLogger("Initializer");

    @Autowired
    public Initializer(UserService userService, CategoryService categoryService,
                       EventServicePrivate eventServicePrivate, EventServiceAdmin eventServiceAdmin, EventRepository eventRepository,
                       PasswordEncoder passwordEncoder
                       ) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventServiceAdmin = eventServiceAdmin;
        this.eventRepository = eventRepository;
        this.eventServicePrivate = eventServicePrivate;
        this.passwordEncoder = passwordEncoder;

        logger.setLevel(Level.ALL);
    }

    @Override
    public void run(String... args) throws Exception {
        Collection<UserDto> users = userService.findAll();
        Collection<CategoryDto> categories = categoryService.getCategories(0, 10);

        if (users == null || users.size() == 0) {
            logger.info("Users are empty, creating the default one!");
            createDefaultUsers();
        } else {
            logger.info("Users found:");
        }

        if (categories == null || categories.size() == 0) {
            logger.info("Categories are empty, creating the default categories!");
            createDefaultCategories();
        }

        //TODO: to delete, only for test
        users = userService.findAll();
        categories = categoryService.getCategories(0, 10);
        Collection<Event> events = eventRepository.findAll();
        if (events == null || events.size() == 0) {
            for (int i = 1; i < 100; i++) {
                EventAddDto eventDto = new EventAddDto(
                new StringBuilder("Title").append(i).toString(),
                new StringBuilder("Annotation").append(i).toString(),
                categories.stream().findAny().get().getId(),
                new StringBuilder("Description").append(i).toString(),
                LocalDateTime.now().plusDays(i),
                LocalDateTime.now().plusDays(i).plusHours(i + 1),
                false,
                i + 1,
                false,
                new EventAddDto.Location(Double.valueOf(i), Double.valueOf(i + 1))
                );
                Long eventId = eventServicePrivate
                        .addEvent(userService.getUserByName("admin1").get().getId(), eventDto).get().getId();
                eventServiceAdmin.publishEvent(eventId);
            }

            for (int i = 11; i < 100; i++) {
                EventAddDto eventDto = new EventAddDto(
                        new StringBuilder("Title").append(i).toString(),
                        new StringBuilder("Annotation").append(i).toString(),
                        categories.stream().findAny().get().getId(),
                        new StringBuilder("Description").append(i).toString(),
                        LocalDateTime.now().plusDays(i),
                        LocalDateTime.now().plusDays(i).plusHours(i + 3),
                        true,
                        i + 1,
                        false,
                        new EventAddDto.Location(Double.valueOf(i), Double.valueOf(i + 1))
                );
                Long eventId = eventServicePrivate
                        .addEvent(userService.getUserByName("user1").get().getId(), eventDto).get().getId();
                eventServiceAdmin.publishEvent(eventId);
            }
        }

    }

    private void createDefaultUsers() {
        User user = User.builder()
                .name("admin1")
                .firstname("ivan")
                .lastname("petrov")
                .email("admin@ewm.ru")
                .password_hash(passwordEncoder.encode("cGFzc3dvcmQ="))
                .build();
        userService.saveUser(user);

        user = User.builder()
                .name("user1")
                .firstname("petr")
                .lastname("ivanov")
                .email("user@ewm.ru")
                .password_hash(passwordEncoder.encode("2cGFzc3dvcmQ="))
                .build();
        userService.saveUser(user);
    }

    private void createDefaultCategories() {
        List<String> names = List.of("Кино", "Рыбалка", "Театр", "Музыка", "Спорт");

        for (String s : names) {
            categoryService.addCategoryByAdmin(Category.builder()
                            .name(s).build());
        }
    }
}
