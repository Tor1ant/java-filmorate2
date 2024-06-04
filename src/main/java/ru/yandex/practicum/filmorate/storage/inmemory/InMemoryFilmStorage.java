package ru.yandex.practicum.filmorate.storage.inmemory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
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

    @Override
    public List<Film> getPopular(Long count) {
        count = count == null ? 10 : count;
        List<Film> top = getAll().stream()
                .filter(f -> !f.getLikes().isEmpty())
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .toList();
        if (top.isEmpty()) {
            top = getAll().stream().limit(count).toList();
        }
        return top;
    }
}
