package ru.yandex.practicum.filmorate.storage;

import java.util.Set;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;

public interface FilmGenreStorage {

    void add(Long filmId, Set<Genre> genres);

    Set<Genre> getGenresByFilmId(Long filmId);
}
