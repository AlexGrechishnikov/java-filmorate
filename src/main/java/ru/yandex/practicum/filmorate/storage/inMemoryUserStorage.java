package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class inMemoryUserStorage implements UserStorage {
    private final HashSet<User> users = new HashSet<>();
    private static long idCounter = 0;

    @Override
    public List<User> findAllUsers() {
        return List.copyOf(users);
    }

    @Override
    public User findUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().compareTo(id) == 0)
                .findAny()
                .orElseThrow(() -> {
                    throw new UserNotFoundException(id);
                });
    }

    @Override
    public User createUser(User user) {
        if (!users.contains(user)) {
            user.setId(++idCounter);
            users.add(user);
            log.info("Добавлен пользователь: {}", user);
            return user;
        } else {
            throw new AlreadyExistsException(user);
        }
    }

    @Override
    public User updateUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            users.add(user);
            log.info("Обновлён пользователь: {}", user);
            return user;
        } else {
            return createUser(user);
        }
    }
}