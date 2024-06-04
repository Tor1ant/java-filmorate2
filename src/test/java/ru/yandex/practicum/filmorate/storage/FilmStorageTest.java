package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тестирование хранилища пользователей")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmStorageTest {

    private final Film film = Film.builder()
            .name("test")
            .description("test")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(100L)
            .mpa(MPA.PG)
            .build();

    @Autowired
    private FilmDao filmDao;

    @Test
    @DisplayName("Создание фильма")
    void testCreateFilm() {
        Film createdFilm = filmDao.create(film);

        Assertions.assertThat(createdFilm.getId()).isEqualTo(1L);
        Assertions.assertThat(createdFilm.getName()).isEqualTo(film.getName());
        Assertions.assertThat(createdFilm.getDescription()).isEqualTo(film.getDescription());
        Assertions.assertThat(createdFilm.getReleaseDate()).isEqualTo(film.getReleaseDate());
        Assertions.assertThat(createdFilm.getDuration()).isEqualTo(film.getDuration());
        Assertions.assertThat(createdFilm.getMpa()).isEqualTo(film.getMpa());
    }

    @Test
    @DisplayName("Обновление фильма")
    void testUpdateFilm() {
        Film createdFilm = filmDao.create(film);
        createdFilm.setName("test2");
        createdFilm.setDescription("test2");
        createdFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        createdFilm.setDuration(101L);
        createdFilm.setMpa(MPA.PG);

        Film updatedFilm = filmDao.update(createdFilm);

        Assertions.assertThat(updatedFilm).isEqualTo(createdFilm);
    }

    @Test
    @DisplayName("Получение фильма")
    void testGetFilm() {
        Film createdFilm = filmDao.create(film);
        Film filmFromDb = filmDao.getById(createdFilm.getId());
        Assertions.assertThat(filmFromDb).isEqualTo(createdFilm);
    }

    @Test
    @DisplayName("Удаление фильма")
    void testDeleteFilm() {
        Film createdFilm = filmDao.create(film);
        Film deletedFilm = filmDao.delete(createdFilm.getId());
        Assertions.assertThat(deletedFilm).isEqualTo(createdFilm);
    }

    @Test
    @DisplayName("Получение списка фильмов")
    void testGetAllFilms() {
        Film createdFilm = filmDao.create(film);
        List<Film> films = filmDao.getAll();
        Assertions.assertThat(films).containsOnly(createdFilm);
    }

    @Test
    @DisplayName("Получение популярных фильмов")
    void testGetPopular() {
        Film createdFilm = filmDao.create(film);
        List<Film> films = filmDao.getPopular(1L);
        Assertions.assertThat(films).containsOnly(createdFilm);
    }
}
