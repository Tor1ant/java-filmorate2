package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.Genre;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.FilmGenreDao;

@SpringBootTest
@AutoConfigureTestDatabase
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Тестирование хранилища пользователей")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmGenreDaoTest {

    @Autowired
    private FilmGenreDao filmGenreDao;
    @Autowired
    private FilmDao filmDao;

    private final Film film = Film.builder()
            .name("test")
            .description("test")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(100L)
            .mpa(MPA.PG)
            .build();

    @DisplayName("Тестирование получения жанров к фильма")
    @Test
    void getGenresByFilmId() {
        Film createdFilm = filmDao.create(film);
        filmGenreDao.add(createdFilm.getId(), Set.of(Genre.DRAMA));

        Set<Genre> genres = filmGenreDao.getGenresByFilmId(createdFilm.getId());
        Assertions.assertThat(genres)
                .hasSize(1)
                .containsOnly(Genre.DRAMA);
    }
}
