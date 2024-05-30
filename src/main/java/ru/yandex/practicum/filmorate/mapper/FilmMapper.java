package ru.yandex.practicum.filmorate.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.generated.model.dto.FilmDTO;
import ru.yandex.practicum.generated.model.dto.GenreDTO;
import ru.yandex.practicum.generated.model.dto.MPADTO;
import ru.yandex.practicum.generated.model.dto.MPADTO.NameEnum;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface FilmMapper {

    @Mapping(target = "mpa", expression = "java(mapMpa(film.getMpa()))")
    @Mapping(target = "genres", expression = "java(mapGenres(film.getGenres()))")
    FilmDTO toDto(Film film);

    @Mapping(target = "releaseDate", expression = "java(mapReleaseDate(filmDTO))")
    @Mapping(target = "genres", expression = "java(mapGenres(filmDTO.getGenres()))")
    @Mapping(target = "mpa", expression = "java(mapMpa(filmDTO.getMpa()))")
    Film toEntity(FilmDTO filmDTO);

    List<FilmDTO> toDto(Collection<Film> films);

    @Mapping(target = "genres", expression = "java(mapGenres(filmDTO.getGenres()))")
    @Mapping(target = "mpa", expression = "java(mapMpa(filmDTO.getMpa()))")
    Film updateFilm(@MappingTarget Film filmForUpdate, FilmDTO filmDTO);

    default LocalDate mapReleaseDate(FilmDTO filmDTO) {
        return LocalDate.parse(filmDTO.getReleaseDate());
    }

    default Set<Genre> mapGenres(List<GenreDTO> genres) {
        return genres.stream().map(it -> Genre.fromId(it.getId().intValue())).collect(Collectors.toSet());
    }

    default MPA mapMpa(MPADTO mpadto) {
        return MPA.fromId(mpadto.getId().intValue());
    }

    default MPADTO mapMpa(MPA mpa) {
        MPADTO mpadto = new MPADTO();
        mpadto.setId((long) mpa.getId());
        mpadto.setName(NameEnum.fromValue(mpa.getValue()));
        return mpadto;
    }

    default List<GenreDTO> mapGenres(Set<Genre> genres) {
        return genres.stream().map(it -> {
                    GenreDTO genreDTO = new GenreDTO();
                    genreDTO.setId((long) it.getId());
                    genreDTO.setNameEng(GenreDTO.NameEngEnum.fromValue(it.name()));
                    genreDTO.setName(it.getRuName());
                    return genreDTO;
                }
        ).collect(Collectors.toList());
    }
}

