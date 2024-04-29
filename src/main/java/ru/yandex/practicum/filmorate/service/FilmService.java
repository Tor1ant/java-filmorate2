package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

public interface FilmService {

    FilmDTO create(FilmDTO film);

    FilmDTO update(FilmDTO film);

    FilmDTO getById(Long id);

    FilmDTO delete(Long id);

    List<FilmDTO> getAll();

    /**
     * Один пользователь может поставить только 1 лайк фильму
     *
     * @param userId id пользователя
     * @param filmId id фильма
     * @return количество поставленных лайков
     */
    Long addLike(Long userId, Long filmId);

    Long deleteLike(Long userId, Long filmId);

    List<FilmDTO> getPopular(Long count);

}
