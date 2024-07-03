package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.entity.film.Film;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film getById(Long id);

    Film delete(Long id);

    List<Film> getAll();

    List<Film> getPopular(Long count);
}
