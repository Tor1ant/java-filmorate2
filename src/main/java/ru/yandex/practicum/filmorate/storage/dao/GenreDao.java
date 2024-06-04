package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.storage.EnumStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDao implements EnumStorage<Genre> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getEnumByEntityId(Long id) {
        log.info("Запрос на получение жанра по id {}", id);
        String sql = """
                select id, name from GENRES
                where id = (select GENRE_ID from films where id =?)
                """;
        try {
            return jdbcTemplate.queryForObject(sql, Genre.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        log.info("Запрос в базу данных на получение всех MPA");
        String sql = "select name from GENRES";
        return jdbcTemplate.queryForList(sql, String.class).stream().map(Genre::valueOf).toList();
    }

    @Override
    public Genre getById(Long id) {
        log.info("Запрос в базу данных на получение MPA по id={}", id);
        String sql = "select name from GENRES where id = ?";
        return jdbcTemplate.queryForObject(sql, Genre.class, id);
    }
}
