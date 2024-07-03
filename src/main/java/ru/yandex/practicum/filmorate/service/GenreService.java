package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.generated.model.dto.GenreDTO;

public interface GenreService {

    List<GenreDTO> getAll();

    GenreDTO getGenreById(Long id);
}
