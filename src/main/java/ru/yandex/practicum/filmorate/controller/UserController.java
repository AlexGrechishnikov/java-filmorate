package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashSet<User> users = new HashSet<>();

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(users);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User newUser) {
        checkUser(newUser);
        if (users.add(newUser)) {
            return newUser;
        } else {
            AlreadyExistsException ex = new AlreadyExistsException(newUser);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.remove(user);
        checkUser(user);
        users.add(user);
        return user;
    }

    private void checkUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
    }
}
