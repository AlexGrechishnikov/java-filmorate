package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public Film createFilm(Film film) {
        checkFilm(film);
        return filmStorage.createFilm(film);
    }

    private void checkFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new CustomValidationException(
                    String.format("Ошибочная дата релиза: %s", film.getReleaseDate().toString()));
        }
        if (film.getDuration() < 0) {
            throw new CustomValidationException("Длительность фильма не может быть отрицательной");
        }
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userService.findUserById(userId);
        film.getLikesIds().add(user.getId());
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userService.findUserById(userId);
        film.getLikesIds().remove(user.getId());
    }

    public List<Film> findMostPopularFilms(Long count) {
        return filmStorage.findAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikesIds().size() - f1.getLikesIds().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}