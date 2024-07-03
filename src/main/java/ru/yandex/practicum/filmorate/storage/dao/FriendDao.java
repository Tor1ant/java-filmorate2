package ru.yandex.practicum.filmorate.storage.dao;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.UserRowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDao implements FriendStorage {

    private static final String TABLE_NAME = "friends";
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public boolean addFriendRequest(Long id, Long friendId) {
        getFriends(id).stream().filter(u -> u.getId().equals(friendId)).findAny().ifPresent(u -> {
            final String message =
                    "Запрос от пользователя с id:" + id + " к пользователю с id:" + friendId + " % уже существует";
            log.warn(message);
            throw new ValidationException(message);
        });

        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, friend_id) VALUES (?, ?)";
        log.info(
                "Добавление запроса на дружбу в базу данных от пользователя с id={} пользователю с id={}",
                id, friendId);
        jdbcTemplate.update(sql, id, friendId);
        log.debug("Запрос на дружбу добавлен в базу данных от пользователя с id={} пользователю с id={}", id, friendId);
        return true;
    }

    @Override
    public boolean deleteFriendRequest(Long id, Long friendId) {
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
        log.info("Получение из базы данных списка общих друзей пользователя с id={} и пользователя с id={}", id,
                friendId);
        String sql = """
                select distinct u.id, u.email, u.login, u.name, u.birthday from users as u
                join friends as f on  u.id = f.friend_id
                where f.user_id in (?,?) and f.friend_id not in(?,?)
                """;

        List<User> commonFriends = jdbcTemplate.queryForList(sql, id, friendId, id, friendId)
                .stream()
                .map(e -> User.builder()
                        .id((Long) e.get("id"))
                        .email((String) e.get("email"))
                        .login((String) e.get("login"))
                        .name((String) e.get("name"))
                        .birthday(LocalDate.parse(e.get("birthday").toString()))
                        .build()).toList();

        log.debug("Получен список общих друзей пользователя с id={} и пользователя с id={}: {}", id, friendId,
                commonFriends);
        return commonFriends;
    }
}
