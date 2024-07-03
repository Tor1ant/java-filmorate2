package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Set;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;

public interface FilmGenreStorage {

    void add(Long filmId, List<Genre> genres);

    Set<Genre> getGenresByFilmId(Long filmId);
}
