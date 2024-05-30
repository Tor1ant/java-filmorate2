package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.List;
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
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тестирование хранилища MPA")
@TestConstructor(autowireMode = AutowireMode.ALL)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTest {

    private final MpaDao mpaStorage;
    private final FilmDao filmDao;


    @Test
    @DisplayName("Тестирование getEnumByEntityId")
    void getEnumByEntityId() {
        Film film = Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100L)
                .mpa(MPA.PG)
                .build();
        Film createdFilm = filmDao.create(film);

        MPA mpa = mpaStorage.getEnumByEntityId(createdFilm.getId());
        Assertions.assertThat(mpa).isEqualTo(MPA.PG);
    }


    @Test
    @DisplayName("Тестирование getAll")
    void getAll() {
        List<MPA> mpaList = mpaStorage.getAll();
        Assertions.assertThat(mpaList).hasSize(5);
    }

    @Test
    @DisplayName("Тестирование getById")
    void getById() {
        MPA mpa = mpaStorage.getById(1L);
        Assertions.assertThat(mpa).isEqualTo(MPA.G);
    }
}
