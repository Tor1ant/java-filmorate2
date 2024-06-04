package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тестирование хранилища Genres")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LikesDaoTest {

    @Autowired
    private LikesStorage likesStorage;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private UserDao userDao;

    private static final User user = User.builder()
            .email("test@mail")
            .login("test")
            .name("test")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    private static final Film film = Film.builder()
            .name("test")
            .description("test")
            .releaseDate(LocalDate.of(2000, 1, 1))
            .duration(100L)
            .mpa(MPA.PG)
            .build();


    @DisplayName("Тестирование получения лайков к фильму с id 1")
    @Test
    void getLikes() {
        filmDao.create(film);
        userDao.create(user);

        likesStorage.addLike(1, 1);
        Assertions.assertThat(likesStorage.getLikes(1))
                .size().isEqualTo(1);
    }

    @DisplayName("Тестирование удаления лайка")
    @Test
    void removeLike() {
        filmDao.create(film);
        userDao.create(user);
        likesStorage.addLike(1, 1);
        likesStorage.removeLike(1, 1);
        Assertions.assertThat(likesStorage.getLikes(1))
                .size().isEqualTo(0);
    }
}
