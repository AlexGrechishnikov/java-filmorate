package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAllFilms();
    Film findFilmById(Long id);
    Film createFilm(Film film);
    Film updateFilm(Film film);
}