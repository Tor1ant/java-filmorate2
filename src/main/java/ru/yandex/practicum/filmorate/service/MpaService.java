package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.generated.model.dto.MPADTO;

public interface MpaService {

    List<MPADTO> getAll();

    MPADTO getMpaById(Long id);
}
