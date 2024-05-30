package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.EnumStorage;
import ru.yandex.practicum.generated.model.dto.GenreDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    @Qualifier("genreDao")
    private final EnumStorage<Genre> genreStorage;

    @Override
    public List<GenreDTO> getAll() {
        return genreStorage.getAll().stream().map(it -> {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setId((long) it.getId());
            genreDTO.setNameEng(GenreDTO.NameEngEnum.fromValue(it.name()));
            genreDTO.setName(it.getRuName());
            return genreDTO;
        }).toList();
    }

    @Override
    public GenreDTO getGenreById(Long id) {
        Genre genre = genreStorage.getById(id);
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(id);
        genreDTO.setNameEng(GenreDTO.NameEngEnum.fromValue(genre.name()));
        genreDTO.setName(genre.getRuName());
        return genreDTO;
    }
}
