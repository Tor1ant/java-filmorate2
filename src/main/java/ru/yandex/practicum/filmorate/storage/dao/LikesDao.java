package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikesDao implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        log.info("Добавление лайка к фильму с id {} пользователем с id {}", filmId, userId);
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Long> getLikes(long filmId) {
        log.info("Получение лайков к фильму с id {}", filmId);
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.info("Удаление лайка к фильму с id {} пользователем с id {}", filmId, userId);
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}

