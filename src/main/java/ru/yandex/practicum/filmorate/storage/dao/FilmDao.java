package ru.yandex.practicum.filmorate.storage.dao;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.EnumStorage;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.dao.mapper.FilmRowMapper;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor()
public class FilmDao implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName = "films";
    private final FilmRowMapper rowMapper;
    @Qualifier("mpaDao")
    private final EnumStorage<MPA> mpaStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Override
    public Film create(Film film) {
        log.info("Создание фильма в базе данных: {}", film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");

        long id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        filmGenreStorage.add(id, film.getGenres());
        log.debug("Фильм создан");
        return getById(id);
    }

    @Override
    public Film update(Film film) {
        log.info("Обновление фильма в базе данных: {}", film);
        String updateQuery = "UPDATE " + tableName
                             + " SET name = ?, description = ?, release_date = ?, duration = ?,mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(updateQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        log.debug("фильм с id={} обновлен", film.getId());
        return getById(film.getId());
    }

    @Override
    public Film getById(Long id) {
        log.info("Получение фильма из базы данных по id: {}", id);
        String selectQuery = "SELECT * FROM " + tableName + " WHERE id = " + id;
        Film film = jdbcTemplate.queryForObject(selectQuery, rowMapper);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден") {
            };
        }
        setMpa(film);
        setGenres(film);
        setLikes(film);
        log.debug("фильм получен из базы данных: {}", film);
        return film;
    }

    @Override
    public Film delete(Long id) {
        log.info("Удаление фильма из базы данных по id: {}", id);
        Film deletedFilm = getById(id);
        String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + id;
        jdbcTemplate.update(deleteQuery);
        return deletedFilm;
    }

    @Override
    public List<Film> getAll() {
        log.info("Получение всех фильмов из базы данных");
        String selectQuery = "SELECT f.*, " +
                             "(SELECT mpa_id FROM films WHERE id = f.id) AS mpa_id, " +
                             "(SELECT ARRAY_AGG(genre_id) FROM films_genres WHERE film_id = f.id) AS genres, " +
                             "(SELECT ARRAY_AGG (USER_ID) FROM LIKES WHERE film_id = f.id) AS likes " +
                             "FROM films f";
        List<Film> films = getList(selectQuery);
        log.debug("Фильмы получены из базы данных: {}", films);
        return films;
    }

    private List<Film> getList(String selectQuery) {
        return jdbcTemplate.query(selectQuery, (resultSet, rowNum) -> {
            Film film = rowMapper.mapRow(resultSet, rowNum);
            Optional.ofNullable(film)
                    .ifPresent(f -> {
                        try {
                            Set<Genre> genres = new HashSet<>();
                            Set<Long> likes = new HashSet<>();

                            f.setMpa(MPA.fromId(resultSet.getInt("mpa_id")));

                            Array array = resultSet.getArray("genres");
                            if (array != null) {
                                ResultSet genresResultSet = array.getResultSet();
                                while (genresResultSet.next()) {
                                    genres.add(Genre.fromId(genresResultSet.getInt("VALUE")));
                                }
                            }
                            Array likesArray = resultSet.getArray("likes");
                            if (likesArray != null) {
                                ResultSet likesResultSet = likesArray.getResultSet();
                                while (likesResultSet.next()) {
                                    likes.add(likesResultSet.getLong("VALUE"));
                                }
                            }
                            film.getLikes().addAll(likes);
                            film.getGenres().addAll(genres);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    });
            return film;
        });
    }

    @Override
    public List<Film> getPopular(Long count) {
        log.info("Получение {} самых популярных фильмов", count);
        count = count == null ? 10 : count;
        String selectQuery = "SELECT f.*, " +
                             "(SELECT mpa_id FROM films WHERE id = f.id) AS mpa_id, " +
                             "(SELECT ARRAY_AGG(genre_id) FROM films_genres WHERE film_id = f.id) AS genres, " +
                             "(SELECT ARRAY_AGG (USER_ID) FROM LIKES WHERE film_id = f.id) AS likes " +
                             "FROM films f "
                             + "ORDER BY likes DESC LIMIT " + count;
        List<Film> films = getList(selectQuery);
        log.debug("Популярные фильмы получены из базы данных: {}", films);
        return films;

    }

    private void setMpa(Film film) {
        film.setMpa(mpaStorage.getEnumByEntityId(film.getId()));
    }

    private void setGenres(Film film) {
        film.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
    }

    private void setLikes(Film film) {
        film.getLikes().addAll(likesStorage.getLikes(film.getId()));
    }
}
