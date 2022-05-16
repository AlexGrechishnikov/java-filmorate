package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();
    User findUserById(Long id);
    User createUser(User user);
    User updateUser(User user);
}