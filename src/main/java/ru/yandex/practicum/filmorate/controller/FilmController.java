package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashSet<Film> films = new HashSet<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return List.copyOf(films);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        checkFilm(newFilm);
        if (films.add(newFilm)) {
            return newFilm;
        }
        return null;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updateFilm) {
        checkFilm(updateFilm);
        films.remove(updateFilm);
        films.add(updateFilm);
        return updateFilm;
    }

    private void checkFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new CustomValidationException(
                    String.format("Ошибочная дата релиза: %s",
                            film.getReleaseDate().toString()));
        }

        if (film.getDuration().isNegative()) {
            throw new CustomValidationException("Длительность фильма не может быть отрицательной");
        }
    }
}

