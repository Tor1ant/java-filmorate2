package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.generated.api.GenresApi;
import ru.yandex.practicum.generated.model.dto.GenreDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController implements GenresApi {

    private final GenreService genreService;

    @Override
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        log.info("Запрос на получение списка жанров");
        return ResponseEntity.ok(genreService.getAll());
    }

    @Override
    public ResponseEntity<GenreDTO> getGenreById(Long id) {
        log.info("Запрос на получение жанра по id: {}", id);
        GenreDTO genre = genreService.getGenreById(id);
        log.debug("Жанр с id={} получен: {}", id, genre);
        return ResponseEntity.ok(genre);
    }
}
