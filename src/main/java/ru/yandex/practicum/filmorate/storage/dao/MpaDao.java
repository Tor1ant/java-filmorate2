package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.EnumStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDao implements EnumStorage<MPA> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA getEnumByEntityId(Long id) {
        log.info("Запрос в базу данных на получение MPA по filmId={}", id);
        String sql = """
                select name from MPA
                where id = (select mpa_id from films where id =?)
                """;
        try {
            return jdbcTemplate.queryForObject(sql, MPA.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<MPA> getAll() {
        log.info("Запрос в базу данных на получение всех MPA");
        String sql = "select name from MPA";
        return jdbcTemplate.queryForList(sql, String.class).stream().map(MPA::valueOf).toList();
    }

    @Override
    public MPA getById(Long id) {
        log.info("Запрос в базу данных на получение MPA по id={}", id);
        String sql = "select name from MPA where id = ?";
        return jdbcTemplate.queryForObject(sql, MPA.class, id);
    }
}
