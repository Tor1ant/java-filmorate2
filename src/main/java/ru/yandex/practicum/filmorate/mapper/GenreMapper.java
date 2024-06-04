package ru.yandex.practicum.filmorate.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.generated.model.dto.GenreDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface GenreMapper {

    default List<Genre> toGenreList(List<GenreDTO> genreDTOList) {
        return genreDTOList.stream().map(it -> Genre.fromId(it.getId().intValue())).collect(Collectors.toList());
    }
}
