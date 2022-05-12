package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class inMemoryFilmStorage implements FilmStorage {
    private final HashSet<Film> films = new HashSet<>();
    private static long idCounter = 0;

    @Override
    public List<Film> findAllFilms() {
        return List.copyOf(films);
    }

    @Override
    public Film findFilmById(Long id) {
        return films.stream()
                .filter(film -> film.getId().compareTo(id) == 0)
                .findAny()
                .orElseThrow(() -> {
                    throw new FilmNotFoundException(id);
                });
    }

    @Override
    public Film createFilm(Film film) {
        if (!films.contains(film)) {
            film.setId(++idCounter);
            films.add(film);
            log.info("Добавлен фильм: {}", film);
            return film;
        } else {
            throw new AlreadyExistsException(film);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.contains(film)) {
            films.remove(film);
            films.add(film);
            log.info("Обновлён фильм: {}", film);
            return film;
        } else {
            return createFilm(film);
        }
    }
}