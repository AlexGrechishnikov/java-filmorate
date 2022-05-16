package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.inMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmService filmService;
    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    void setUp() {
        filmService = new FilmService(new inMemoryFilmStorage(),
                new UserService(new inMemoryUserStorage()));
    }

    @Test
    void getAllFilms() {
        Film film = new Film(1L, "name", "descr",
                LocalDate.of(2000, Month.AUGUST, 1), 100);
        List<Film> films = List.of(film);

        filmService.createFilm(film);
        List<Film> allFilmsFromController = filmService.findAllFilms();

        assertEquals(films, allFilmsFromController, "Возвращаемый список фильмов из контроллера отличатся");
    }

    @Test
    void addWrongReleaseFilm() {
        Film wrongReleaseFilm = new Film(3L, "film", "veryLongDescription",
                LocalDate.of(1888, Month.AUGUST, 1), 100);

        final CustomValidationException exception =
                assertThrows(CustomValidationException.class, () -> filmService.createFilm(wrongReleaseFilm));

        assertTrue(exception.getMessage().contains("Ошибочная дата релиза"));
    }

    @Test
    void addWrongDurationFilm() {
        Film wrongDurationFilm = new Film(3L, "film", "veryLongDescription",
                LocalDate.of(1988, Month.AUGUST, 1), -100);

        final CustomValidationException exception =
                assertThrows(CustomValidationException.class, () -> filmService.createFilm(wrongDurationFilm));

        assertTrue(exception.getMessage().contains("Длительность фильма не может быть отрицательной"));
    }

    @Test
    void validateWrongDescriptionNameFilm() {
        Film wrongDescrFilm = new Film(3L, "film", "veryLongDescription".repeat(11),
                LocalDate.of(2000, Month.AUGUST, 1), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(wrongDescrFilm);

        assertFalse(violations.isEmpty());
    }

    @Test
    void validateEmptyNameFilm() {
        Film wrongNameFilm = new Film(2L, "", "descr",
                LocalDate.of(2000, Month.AUGUST, 1), 100);

        Set<ConstraintViolation<Film>> violations = validator.validate(wrongNameFilm);

        assertFalse(violations.isEmpty());
    }

    @Test
    void addNewOrUpdateFilm() {
        Film film = new Film(1L, "name", "descr",
                LocalDate.of(2000, Month.AUGUST, 1), 100);

        Film filmFromController = filmService.updateFilm(film);

        assertEquals(film, filmFromController, "Фильмы должны быть одинаковы");

        filmFromController.setName("Измененное имя");
        Film secondUpdateFilm = filmService.updateFilm(filmFromController);

        assertEquals(film, secondUpdateFilm, "Фильмы должны быть одинаковы");
    }

    @Test
    void exFromPOSTForTwoIdenticalFilms() {
        Film firstFilm = new Film(1L, "name", "descr",
                LocalDate.of(2000, Month.AUGUST, 1), 100);
        Film secondFilm = filmService.createFilm(firstFilm);

        assertEquals(firstFilm, secondFilm, "Фильмы должны быть одинаковы");

        AlreadyExistsException exception =
                assertThrows(AlreadyExistsException.class, () -> filmService.createFilm(secondFilm));

        assertTrue(exception.getMessage().contains("уже существует"));
    }
}