package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserService userService;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(new inMemoryUserStorage());
    }

    @Test
    void getAllUsers() {
        User user = new User(1L, "user@mail.ru", "user",
                "Ivan", LocalDate.of(2000, Month.FEBRUARY, 23));
        List<User> users = List.of(user);

        userService.createUser(user);
        List<User> allUsersFromController = userService.findAllUsers();

        assertEquals(users, allUsersFromController, "Списки должны быть одинаковыми");
    }

    @Test
    void exFromPOSTForTwoIdenticalUsers() {
        User firstUser = new User(1L, "user@mail.ru", "user",
                "Ivan", LocalDate.of(2000, Month.FEBRUARY, 23));
        User secondUser = new User(2L, "u12ser@mail.ru", "user",
                "Fedor", LocalDate.of(2010, Month.FEBRUARY, 23));

        userService.createUser(firstUser);
        AlreadyExistsException ex =
                assertThrows(AlreadyExistsException.class, () -> userService.createUser(secondUser));
        assertTrue(ex.getMessage().contains("уже существует"));
    }

    @Test
    void addAndUpdateUser() {
        User user = new User(1L, "user@mail.ru", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));

        User userFromController = userService.updateUser(user);
        userFromController.setEmail("ivan@mail.ru");
        User updateUser = userService.updateUser(userFromController);
        List<User> userControllerAllUsers = userService.findAllUsers();

        assertEquals(user, updateUser);
        assertEquals(updateUser.getEmail(), "ivan@mail.ru");
        assertEquals(1, userControllerAllUsers.size());
    }

    @Test
    void validateWrongEmail() {
        User wrongEmailUser = new User(1L, "wrongmail.ru", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));
        User wrongEmailUser2 = new User(1L, "wrong@mail", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));
        User wrongEmailUser3 = new User(1L, "wrong@.ru", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));
        User wrongEmailUser4 = new User(1L, "@mail.ru", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));

        Set<ConstraintViolation<User>> violations = validator.validate(wrongEmailUser);
        Set<ConstraintViolation<User>> violations2 = validator.validate(wrongEmailUser2);
        Set<ConstraintViolation<User>> violations3 = validator.validate(wrongEmailUser3);
        Set<ConstraintViolation<User>> violations4 = validator.validate(wrongEmailUser4);

        assertFalse(violations.isEmpty());
        assertFalse(violations2.isEmpty());
        assertFalse(violations3.isEmpty());
        assertFalse(violations4.isEmpty());
    }

    @Test
    void validateWrongLogin() {
        User wrongLoginUser = new User(1L, "user@mail.ru", "wrong username",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));
        User wrongLoginUser2 = new User(1L, "user@mail.ru", "",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));
        User wrongLoginUser3 = new User(1L, "user@mail.ru", null,
                "", LocalDate.of(2000, Month.FEBRUARY, 23));

        Set<ConstraintViolation<User>> violations = validator.validate(wrongLoginUser);
        Set<ConstraintViolation<User>> violations2 = validator.validate(wrongLoginUser2);
        Set<ConstraintViolation<User>> violations3 = validator.validate(wrongLoginUser3);

        assertFalse(violations.isEmpty());
        assertFalse(violations2.isEmpty());
        assertFalse(violations3.isEmpty());
    }

    @Test
    void validateWrongBirthday() {
        User wrongBirthdayUser = new User(1L, "user@mail.ru", "user",
                "", LocalDate.now());
        User wrongBirthdayUser2 = new User(1L, "user@mail.ru", "user",
                "", LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(wrongBirthdayUser);
        Set<ConstraintViolation<User>> violations2 = validator.validate(wrongBirthdayUser2);

        assertFalse(violations.isEmpty());
        assertFalse(violations2.isEmpty());
    }

    @Test
    void ifEmptyNameThenSetNameLikeLogin() {
        User user = new User(1L, "user@mail.ru", "user",
                "", LocalDate.of(2000, Month.FEBRUARY, 23));

        User userFromController = userService.createUser(user);

        assertEquals("user", userFromController.getName());
    }
}