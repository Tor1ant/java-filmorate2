package ru.yandex.practicum.filmorate.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FilmMapper {

    FilmDTO usersToUserDTO(Film film);

    Film filmDtoToFilm(FilmDTO filmDTO);

    List<FilmDTO> usersToUserDTO(Collection<Film> films);

    Film updateFilm(@MappingTarget Film filmForUpdate, FilmDTO filmDTO);
}
