package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void getAllFilms() {
        Film film = new Film(1L, "name", "descr",
                LocalDate.of(2000, Month.AUGUST, 1), Duration.ZERO);
        List<Film> films = List.of(film);

        filmController.addFilm(film);
        List<Film> allFilmsFromController = filmController.getAllFilms();

        assertEquals(films, allFilmsFromController, "Возвращаемый список фильмов из контроллера отличатся");
    }

//    @Test
//    @Disabled
//    void addFilm() {
//        Film film = new Film(1L, "name", "descr",
//                LocalDate.of(2000, Month.AUGUST, 1), Duration.ZERO);
//        List<Film> films = List.of(film);
//
//        filmController.addFilm(film);
//
//        Film wrongNameFilm = new Film(2L, "", "descr",
//                LocalDate.of(2000, Month.AUGUST, 1), Duration.ZERO);
//        filmController.addFilm(wrongNameFilm);
//
//        Film wrongDescrFilm = new Film(3L, "film", "veryLongDescription".repeat(11),
//                LocalDate.of(2000, Month.AUGUST, 1), Duration.ZERO);
//        filmController.addFilm(wrongDescrFilm);
//
//        List<Film> allFilmsFromController = filmController.getAllFilms();
//
//        assertEquals(films, allFilmsFromController, "Коллекции не совпадают");
//    }

    @Test
    void addWrongReleaseFilm() {
        Film wrongReleaseFilm = new Film(3L, "film", "veryLongDescription",
                LocalDate.of(1888, Month.AUGUST, 1), Duration.ZERO);

        final CustomValidationException exception =
                assertThrows(CustomValidationException.class, () -> filmController.addFilm(wrongReleaseFilm));

        assertTrue(exception.getMessage().contains("Ошибочная дата релиза"));
    }

    @Test
    void addWrongDurationFilm() {
        Film wrongDurationFilm = new Film(3L, "film", "veryLongDescription",
                LocalDate.of(1988, Month.AUGUST, 1), Duration.of(-100, ChronoUnit.MINUTES));

        final CustomValidationException exception =
                assertThrows(CustomValidationException.class, () -> filmController.addFilm(wrongDurationFilm));

        assertTrue(exception.getMessage().contains("Длительность фильма не может быть отрицательной"));
    }
}