package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashSet<User> users = new HashSet<>();

    {
        users.add(new User(1L, "user@mail.ru", "user",
                "Ivan", LocalDate.of(2000, Month.FEBRUARY, 23)));
    }

    @GetMapping
    public List<User> getAllFilms() {
        return List.copyOf(users);
    }

    @PostMapping
    public User addFilm(@Valid @RequestBody User newUser) {
        checkUser(newUser);
        if (users.add(newUser)) {
            return newUser;
        }
        return null;
    }


    @PutMapping
    public User updateFilm(@Valid @RequestBody User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    private void checkUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getEmail());
        }
    }
}
