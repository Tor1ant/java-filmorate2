package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.generated.api.MpaApi;
import ru.yandex.practicum.generated.model.dto.MPADTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MpaController implements MpaApi {

    private final MpaService mpaService;

    @Override
    public ResponseEntity<List<MPADTO>> getAll() {
        log.info("Запрос на получение списка рейтингов");
        return ResponseEntity.ok(mpaService.getAll());
    }

    @Override
    public ResponseEntity<MPADTO> getMpaNameById(Long id) {
        log.info("Запрос на получение MPA по id {}", id);
        return ResponseEntity.ok(mpaService.getMpaById(id));
    }
}
