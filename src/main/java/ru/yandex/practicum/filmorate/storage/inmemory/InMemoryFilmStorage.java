package ru.yandex.practicum.filmorate.storage.inmemory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    private Long id = 0L;


    @Override
    public Film create(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

    @Override
    public Film delete(Long id) {
        return films.remove(id);
    }

    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }
}
