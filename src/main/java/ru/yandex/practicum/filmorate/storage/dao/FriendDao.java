package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.UserRowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDao implements FriendStorage {

    private static final String TABLE_NAME = "friends";
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public boolean addFriendRequest(Long id, Long friendId) {
        userStorage.getById(id);
        userStorage.getById(friendId);
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, friend_id) VALUES (?, ?)";
        log.info(
                "Добавление запроса на дружбу в базу данных от пользователя с id={} пользователю с id={}",
                id, friendId);
        try {
            jdbcTemplate.update(sql, id, friendId);
        } catch (DataAccessException e) {
            log.error("Запрос на дружбу уже существует в базе данных", e);
            return false;
        }
        log.debug("Запрос на дружбу добавлен в базу данных от пользователя с id={} пользователю с id={}", id, friendId);
        return true;
    }

    @Override
    public boolean deleteFriendRequest(Long id, Long friendId) {
        userStorage.getById(id);
        userStorage.getById(friendId);
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ? AND friend_id = ?";
        log.info(
                "Удаление запроса на дружбу из базы данных от пользователя с id={} пользователю с id={}",
                id, friendId);
        jdbcTemplate.update(sql, id, friendId);
        log.debug("Запрос на дружбу удален из базы данных от пользователя с id={} пользователю с id={}", id, friendId);
        return true;
    }

    @Override
    public List<User> getFriends(Long id) {
        if (!userStorage.isExists(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не существует");
        }

        log.info("Получение из базы данных списка друзей пользователя с id={}", id);
        String sql = """
                select * from users as u
                join friends as f on u.id = f.friend_id
                where f.user_id = ?
                """;
        List<User> friends = jdbcTemplate.query(sql, userRowMapper, id);
        log.debug("Получен список друзей пользователя с id={}: {}", id, friends);
        return friends;
    }


    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        userStorage.getById(id);
        userStorage.getById(friendId);

        log.info("Получение из базы данных списка общих друзей пользователя с id={} и пользователя с id={}", id,
                friendId);
        String sql = """
                select distinct u.id, u.email, u.login, u.name, u.birthday from users as u
                join friends as f on  u.id = f.friend_id
                where f.user_id in (?,?) and f.friend_id not in(?,?)
                """;
        List<User> commonFriends;
        try {
            commonFriends = jdbcTemplate.query(sql, userRowMapper, id, friendId, id, friendId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
        log.debug("Получен список общих друзей пользователя с id={} и пользователя с id={}: {}", id, friendId,
                commonFriends);
        return commonFriends;
    }

    @Override
    public boolean hasFriends(Long id) {
        log.info("Проверка наличия друзей у пользователя с id={}", id);
        String sql = """
                select 1
                from FRIENDS
                where USER_ID = ?
                limit 1
                """;
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Пользователь с id={} не имеет друзей", id);
            return false;
        }
        log.debug("Пользователь с id={} имеет друзей", id);
        return true;
    }
}
