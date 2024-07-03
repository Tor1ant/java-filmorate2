package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmMapper filmMapper;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreMapper genreMapper;

    @Override
    public FilmDTO create(FilmDTO filmDTO) {
        log.info("Создание фильма {}", filmDTO);
        Film filmToCreate = filmMapper.toEntity(filmDTO);
        Film createdFilm = filmStorage.create(filmToCreate);
        filmGenreStorage.add(createdFilm.getId(), genreMapper.toGenreList(filmDTO.getGenres()));
        createdFilm.setGenres(filmGenreStorage.getGenresByFilmId(createdFilm.getId()));
        log.debug("Создан фильм {}", createdFilm);
        return filmMapper.toDto(createdFilm);
    }

    @Override
    public FilmDTO update(FilmDTO filmDTO) {
        log.info("Обновление фильма {}", filmDTO);
        Film filmForUpdate = getEntityById(filmDTO.getId());
        Film updatedFilm = filmMapper.updateFilm(filmForUpdate, filmDTO);
        log.debug("Обновлен фильм {}", updatedFilm);
        updatedFilm = filmStorage.update(updatedFilm);
        return filmMapper.toDto(updatedFilm);
    }

    @Override
    public FilmDTO getById(Long id) {
        log.info("Получение фильма с id {}", id);
        Film film = getEntityById(id);
        log.debug("Получен фильм {}", film);
        return filmMapper.toDto(film);
    }

    @Override
    public FilmDTO delete(Long id) {
        log.info("Удаление фильма с id {}", id);
        Film film = filmStorage.delete(id);
        log.debug("Удален фильм {}", film);
        return filmMapper.toDto(film);
    }

    @Override
    public List<FilmDTO> getAll() {
        log.info("Получение списка всех фильмов");
        List<Film> films = filmStorage.getAll();
        log.debug("Получен список фильмов {}", films);
        return filmMapper.toDto(films);
    }

    @Override
    public Long addLike(Long userId, Long filmId) {
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        userStorage.getById(userId);
        getEntityById(filmId);
        likesStorage.addLike(filmId, userId);
        return (long) likesStorage.getLikes(filmId).size();
    }


    @Override
    public Long deleteLike(Long userId, Long filmId) {
        log.info("Пользователь {} удалил лайк фильму {}", userId, filmId);
        userStorage.getById(userId);
        getEntityById(filmId);
        likesStorage.removeLike(filmId, userId);
        return (long) likesStorage.getLikes(filmId).size();
    }

    @Override
    public List<FilmDTO> getPopular(Long count) {
        count = count == null ? 10 : count;
        log.info("Получение списка популярных фильмов в количестве {}", count);
        return filmMapper.toDto(filmStorage.getPopular(count));
    }

    private Film getEntityById(Long id) {
        Film entity = filmStorage.getById(id);
        if (entity == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        entity.setGenres(filmGenreStorage.getGenresByFilmId(id));
        return entity;
    }
}
