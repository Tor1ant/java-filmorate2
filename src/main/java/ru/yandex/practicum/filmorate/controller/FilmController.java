package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.generated.api.FilmsApi;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController implements FilmsApi {

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmMapper mapper;
    private Long id = 0L;

    @Override
    public ResponseEntity<FilmDTO> createFilm(FilmDTO filmDTO) {
        log.info("Запрос на создание фильма: {}", filmDTO);
        validate(filmDTO);
        Film film = mapper.filmDtoToFilm(filmDTO);
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Создан фильм: {}", film);
        return ResponseEntity.status(201).body(mapper.usersToUserDTO(film));
    }

    @Override
    public ResponseEntity<FilmDTO> updateFilm(FilmDTO filmDTO) {
        log.info("Запрос на обновление фильма: {}", filmDTO);
        validate(filmDTO);
        Film updatedFilm = films.compute(filmDTO.getId(),
                (k, filmForUpdate) -> mapper.updateFilm(filmForUpdate, filmDTO));
        log.debug("Обновлен фильм: {}", updatedFilm);
        return ResponseEntity.ok(mapper.usersToUserDTO(updatedFilm));
    }

    @Override
    public ResponseEntity<FilmDTO> getFilmById(Long id) {
        log.info("Запрос на получение фильма с id: {}", id);
        return ResponseEntity.ok(mapper.usersToUserDTO(films.get(id)));
    }

    @Override
    public ResponseEntity<List<FilmDTO>> getFilms() {
        log.info("Запрос на получение списка фильмов");
        return ResponseEntity.ok(mapper.usersToUserDTO(films.values()));
    }


    private void validate(FilmDTO filmDTO) {
        LocalDate movieBirthday = LocalDate.of(1895, 12, 28);
        if (filmDTO.getReleaseDate().isBefore(movieBirthday)) {
            log.error("Дата релиза фильма не может быть раньше 1895-12-28. Дата релиза: {}", filmDTO.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть раньше 1895-12-28");
        }
        if (filmDTO.getReleaseDate().isAfter(LocalDate.now())) {
            log.error("Дата релиза фильма не может быть в будущем. Дата релиза: {}", filmDTO.getReleaseDate());
            throw new ValidationException("Дата релиза фильма не может быть в будущем");
        }
    }
}
