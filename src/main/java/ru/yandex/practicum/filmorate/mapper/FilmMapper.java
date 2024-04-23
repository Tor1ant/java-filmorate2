package ru.yandex.practicum.filmorate.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FilmMapper {

    FilmDTO toDto(Film film);

    @Mapping(target = "releaseDate", expression = "java(mapReleaseDate(filmDTO))")
    Film toEntity(FilmDTO filmDTO);

    List<FilmDTO> toDto(Collection<Film> films);

    Film updateFilm(@MappingTarget Film filmForUpdate, FilmDTO filmDTO);

    default LocalDate mapReleaseDate(FilmDTO filmDTO) {
        return LocalDate.parse(filmDTO.getReleaseDate());
    }
}
