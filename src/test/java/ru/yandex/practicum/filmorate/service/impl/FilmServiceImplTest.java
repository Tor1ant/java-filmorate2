package ru.yandex.practicum.filmorate.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.generated.model.dto.FilmDTO;
import ru.yandex.practicum.generated.model.dto.MPADTO;
import ru.yandex.practicum.generated.model.dto.MPADTO.NameEnum;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {

    @Mock
    private FilmStorage filmStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private LikesStorage likesStorage;
    @Mock
    private FilmGenreStorage filmGenreStorage;

    private FilmDTO filmDTO;
    private Film filmBeforeCreate;
    private FilmServiceImpl filmService;

    private final FilmMapper filmMapper = Mappers.getMapper(FilmMapper.class);
    private final GenreMapper genreMapper = Mappers.getMapper(GenreMapper.class);

    @BeforeEach
    void setUp() {
        filmService = new FilmServiceImpl(filmMapper, filmStorage, userStorage, likesStorage, filmGenreStorage,
                genreMapper);

        filmDTO = new FilmDTO()
                .id(null)
                .name("Тестовый фильм")
                .description("Тестовое описание")
                .releaseDate("1895-12-28")
                .mpa(new MPADTO().id(1L).name(NameEnum.G))
                .duration(160L);
        filmBeforeCreate = Film.builder()
                .id(null)
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(LocalDate.parse(filmDTO.getReleaseDate()))
                .mpa(MPA.G)
                .duration(filmDTO.getDuration())
                .build();
    }

    @Test
    @DisplayName("Проверка создания фильма")
    void create() {
        Mockito.when(filmStorage.create(filmBeforeCreate))
                .thenAnswer(invocationOnMock -> {
                            filmBeforeCreate.setId(1L);
                            return filmBeforeCreate;
                        }
                );
        Mockito.doNothing()
                .when(filmGenreStorage)
                .add(Mockito.any(), Mockito.any());

        Mockito.when(filmGenreStorage.getGenresByFilmId(Mockito.any()))
                .thenReturn(Set.of());

        FilmDTO result = filmService.create(filmDTO);
        filmDTO.setId(1L);
        Assertions.assertThat(result).isEqualTo(filmDTO);
    }

    @Test
    @DisplayName("Проверка обновления фильма")
    void update() {
        FilmDTO filmDTO = new FilmDTO()
                .id(1L)
                .name("Обновленный фильм")
                .description("Обновленное описание")
                .releaseDate(filmBeforeCreate.getReleaseDate().toString())
                .mpa(new MPADTO().id(1L).name(NameEnum.G))
                .duration(filmBeforeCreate.getDuration());

        Film filmForUpdate = Film.builder()
                .id(filmDTO.getId())
                .name(filmDTO.getName())
                .description(filmDTO.getDescription())
                .releaseDate(LocalDate.parse(filmDTO.getReleaseDate()))
                .mpa(MPA.G)
                .duration(filmBeforeCreate.getDuration())
                .build();

        Mockito.when(filmStorage.getById(1L)).thenAnswer(invocationOnMock -> {
            filmBeforeCreate.setId(1L);
            return filmBeforeCreate;
        });

        Mockito.when(filmStorage.update(filmForUpdate))
                .thenReturn(filmForUpdate);

        FilmDTO result = filmService.update(filmDTO);

        Assertions.assertThat(result).isEqualTo(filmDTO);
    }

    @Test
    @DisplayName("Проверка получения фильма по id")
    void getById() {
        filmDTO.setId(1L);
        filmBeforeCreate.setId(1L);
        Mockito.when(filmStorage.getById(1L)).thenReturn(filmBeforeCreate);

        FilmDTO result = filmService.getById(1L);

        Assertions.assertThat(result).isEqualTo(filmDTO);
    }

    @Test
    @DisplayName("Проверка удаления фильма")
    void delete() {
        filmDTO.setId(1L);
        filmBeforeCreate.setId(1L);

        Mockito.when(filmStorage.delete(1L)).thenReturn(filmBeforeCreate);

        FilmDTO delete = filmService.delete(1L);

        Assertions.assertThat(delete).isEqualTo(filmDTO);
    }

    @Test
    @DisplayName("Проверка получения всех фильмов")
    void getAll() {
        Mockito.when(filmStorage.getAll()).thenReturn(List.of(filmBeforeCreate));

        List<FilmDTO> result = filmService.getAll();

        Assertions.assertThat(result.getFirst()).isEqualTo(filmDTO);
    }

    @Test
    @DisplayName("Проверка добавления лайка")
    void addLike() {
        Mockito.when(filmStorage.getById(1L)).thenReturn(filmBeforeCreate);
        Mockito.when(userStorage.getById(1L)).then(invocationOnMock -> User.builder().id(1L).build());
        Mockito.when(likesStorage.getLikes(1L)).thenReturn(List.of(1L));
        Long likesCount = filmService.addLike(1L, 1L);

        Assertions.assertThat(likesCount).isEqualTo(1L);
        Mockito.verify(likesStorage).addLike(1L, 1L);
    }

    @Test
    @DisplayName("Проверка удаления лайка")
    void deleteLike() {
        filmBeforeCreate.getLikes().add(1L);

        Mockito.when(filmStorage.getById(1L)).thenReturn(filmBeforeCreate);
        Mockito.when(userStorage.getById(1L)).then(invocationOnMock -> User.builder().build());
        Mockito.when(likesStorage.getLikes(1L)).thenReturn(List.of());

        Long likesCount = filmService.deleteLike(1L, 1L);

        Assertions.assertThat(likesCount).isEqualTo(0L);
        Mockito.verify(likesStorage).removeLike(1L, 1L);
    }

    @Test
    @DisplayName("Проверка получения самых популярных фильмов")
    void getPopular() {
        List<Long> likesForFirstFilm = List.of(1L, 2L, 3L, 4L, 5L);
        List<Long> likesForSecondFilm = List.of(1L);
        Film secondFilm = Film.builder()
                .id(2L)
                .name("Второй фильм")
                .description("Второе описание")
                .releaseDate(LocalDate.parse("1899-12-28"))
                .duration(100L)
                .mpa(MPA.G)
                .build();
        FilmDTO secondFilmDto = new FilmDTO()
                .id(secondFilm.getId())
                .name(secondFilm.getName())
                .description(secondFilm.getDescription())
                .releaseDate(secondFilm.getReleaseDate().toString())
                .mpa(new MPADTO().id(1L).name(NameEnum.G))
                .likes(likesForSecondFilm)
                .duration(secondFilm.getDuration());

        filmDTO.getLikes().addAll(likesForFirstFilm);
        filmBeforeCreate.getLikes().addAll(likesForFirstFilm);
        secondFilm.getLikes().addAll(likesForSecondFilm);

        Mockito.when(filmStorage.getPopular(10L)).thenReturn(List.of(filmBeforeCreate, secondFilm));

        List<FilmDTO> result = filmService.getPopular(10L);

        Assertions.assertThat(result).isEqualTo(List.of(filmDTO, secondFilmDto));
    }
}
