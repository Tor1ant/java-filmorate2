package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.UserRowMapper;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor(onConstructor_ = @__(@Lazy))
public class UserDao implements UserStorage {

    @Getter
    private final String tableName = "users";
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;
    private final FriendDao friendDao;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        log.info("Создание пользователя в базе данных: {}", user);
        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        log.debug("Пользователь с id={} создан", id);
        return getById(id);
    }

    @Override
    public User update(User user) {
        log.info("Обновление пользователя в базе данных: {}", user);
        String updateQuery = "UPDATE " + tableName + " SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(updateQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        log.debug("Пользователь с id={} обновлен", user.getId());
        return getById(user.getId());
    }

    @Override
    public User getById(Long id) {
        log.info("Получение пользователя из базы данных по id: {}", id);
        String selectQuery = "SELECT * FROM " + tableName + " WHERE id = " + id;
        User user = jdbcTemplate.queryForObject(selectQuery, rowMapper);

        if (friendDao.hasFriends(id)) {
            Objects.requireNonNull(user).getFriends()
                    .addAll(friendDao.getFriends(id).stream().map(User::getId).toList());
        }
        log.debug("Пользователь получен из базы данных: {}", user);
        return user;
    }

    @Override
    public User delete(Long id) {
        User user = getById(id);
        log.info("Удаление пользователя из базы данных по id: {}", id);
        String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + id;
        jdbcTemplate.update(deleteQuery);
        log.debug("Пользователь с id={} удален из базы данных", id);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("Получение всех пользователей из базы данных");
        String selectQuery = "SELECT * FROM " + tableName;
        List<User> users = jdbcTemplate.query(selectQuery, rowMapper);
        log.debug("Пользователи получены из базы данных: {}", users);
        return users;
    }

    @Override
    public boolean isExists(Long id) {
        log.info("Проверка существования пользователя в базе данных с id={}", id);
        String sql = """
                select 1
                from users
                where id = ?
                limit 1
                """;
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Пользователь с id={} не существует", id);
            return false;
        }
        log.debug("Пользователь с id={} существует", id);
        return true;
    }
}
