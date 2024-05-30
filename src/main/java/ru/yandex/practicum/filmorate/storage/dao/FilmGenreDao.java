package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.GenreRowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmGenreDao implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper rowMapper;

    @Override
    public void add(Long filmId, Set<Genre> genres) {
        log.info("Добавление жанров для фильма filmId = {} genres = {} ", filmId, genres);
        List<Object[]> batchArgs = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, batchArgs);
        log.debug("Жанры для фильма filmId = {} genres = {} добавлены", filmId, genres);
    }

    public Set<Genre> getGenresByFilmId(Long filmId) {
        log.info("Получение жанров для фильма filmId = {}", filmId);
        String sql = "SELECT genre_id FROM films_genres WHERE film_id = ?";

        Set<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query(sql, rowMapper, filmId)).stream()
                .sorted(Comparator.comparingInt(Genre::getId)).collect(
                        Collectors.toCollection(LinkedHashSet::new));
        log.debug("Жанры для фильма filmId = {} получены, genres={}", filmId, genres);
        return genres;
    }
}
