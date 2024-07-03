package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.UserRowMapper;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class UserDao implements UserStorage {

    @Getter
    private final String tableName = "users";
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;

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
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
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
}
