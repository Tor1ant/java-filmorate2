package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.generated.api.FilmsApi;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController implements FilmsApi {

    private final FilmService filmService;

    @Override
    public ResponseEntity<FilmDTO> createFilm(FilmDTO filmDTO) {
        log.info("Запрос на создание фильма: {}", filmDTO);
        validate(filmDTO);
        return ResponseEntity.status(201).body(filmService.create(filmDTO));
    }

    @Override
    public ResponseEntity<FilmDTO> updateFilm(FilmDTO filmDTO) {
        log.info("Запрос на обновление фильма: {}", filmDTO);
        validate(filmDTO);
        return ResponseEntity.ok(filmService.update(filmDTO));
    }

    @Override
    public ResponseEntity<FilmDTO> getFilmById(Long id) {
        log.info("Запрос на получение фильма с id: {}", id);
        return ResponseEntity.ok(filmService.getById(id));
    }

    @Override
    public ResponseEntity<List<FilmDTO>> getFilms() {
        log.info("Запрос на получение списка фильмов");
        return ResponseEntity.ok(filmService.getAll());
    }

    @Override
    public ResponseEntity<Long> addLike(Long id, Long userId) {
        log.info("Запрос на добавление лайка {} пользователю {}", id, userId);
        return ResponseEntity.ok(filmService.addLike(userId, id));
    }

    @Override
    public ResponseEntity<Long> deleteLike(Long id, Long userId) {
        log.info("Запрос на удаление лайка {} пользователю {}", id, userId);
        return ResponseEntity.ok(filmService.deleteLike(userId, id));
    }

    @Override
    public ResponseEntity<List<FilmDTO>> getPopular(Long count) {
        log.info("Запрос на получение списка популярных фильмов");
        return ResponseEntity.ok(filmService.getPopular(count));
    }


    private void validate(FilmDTO filmDTO) {
        LocalDate movieBirthday = LocalDate.of(1895, 12, 28);
        LocalDate releaseDate = LocalDate.parse(filmDTO.getReleaseDate());
        if (releaseDate.isBefore(movieBirthday)) {
            log.error("Дата релиза фильма не может быть раньше 1895-12-28. Дата релиза: {}", filmDTO.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть раньше 1895-12-28");
        }
        if (releaseDate.isAfter(LocalDate.now())) {
            log.error("Дата релиза фильма не может быть в будущем. Дата релиза: {}", filmDTO.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть в будущем");
        }
    }
}
