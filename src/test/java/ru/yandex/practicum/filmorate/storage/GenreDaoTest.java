package ru.yandex.practicum.filmorate.storage;

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
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тестирование хранилища Genres")
@TestConstructor(autowireMode = AutowireMode.ALL)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {

    private final GenreDao genreDao;

    @DisplayName("Тест получения всех жанров")
    @Test
    public void getAll() {
        Assertions.assertThat(genreDao.getAll()).size().isEqualTo(6);
    }

    @DisplayName("Тест получения жанра по id")
    @Test
    public void getById() {
        Assertions.assertThat(genreDao.getById(1L).getRuName()).isEqualTo("Комедия");
    }
}
