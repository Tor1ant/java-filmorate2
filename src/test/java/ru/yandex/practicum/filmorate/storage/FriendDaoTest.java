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
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

@SpringBootTest
@AutoConfigureTestDatabase
@DisplayName("Тестирование хранилища Genres")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendDaoTest {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private UserDao userDao;

    private static final User testUser = User.builder()
            .email("test@mail")
            .login("test")
            .name("test")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();
    private static final User testUser2 = User.builder()
            .email("test@mail2")
            .login("test2")
            .name("test2")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    @Test
    @DisplayName("Добавление в друзья")
    void addFriend() {
        User createdTestUser = userDao.create(testUser);
        User createdTestUser2 = userDao.create(testUser2);

        boolean isFriend = friendDao.addFriendRequest(createdTestUser.getId(), createdTestUser2.getId());
        Assertions.assertThat(isFriend).isTrue();
    }

    @Test
    @DisplayName("Удаление из друзей")
    void deleteFriend() {
        User createdTestUser = userDao.create(testUser);
        User createdTestUser2 = userDao.create(testUser2);
        friendDao.addFriendRequest(createdTestUser.getId(), createdTestUser2.getId());
        boolean deleted = friendDao.deleteFriendRequest(createdTestUser.getId(), createdTestUser2.getId());
        Assertions.assertThat(deleted).isTrue();
    }

    @Test
    @DisplayName("Получение друзей")
    void getFriends() {
        User createdTestUser = userDao.create(testUser);
        User createdTestUser2 = userDao.create(testUser2);
        friendDao.addFriendRequest(createdTestUser.getId(), createdTestUser2.getId());
        Assertions.assertThat(friendDao.getFriends(createdTestUser.getId()).size()).isEqualTo(1);
    }


    @Test
    @DisplayName("Проверка наличия общих друзей у пользователей")
    void getCommonFriends() {
        User testUser3 = User.builder()
                .email("test@mail3")
                .login("test3")
                .name("test3")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User createdTestUser = userDao.create(testUser);
        User createdTestUser2 = userDao.create(testUser2);
        User createdTestUser3 = userDao.create(testUser3);

        friendDao.addFriendRequest(createdTestUser.getId(), createdTestUser2.getId());
        friendDao.addFriendRequest(createdTestUser2.getId(), createdTestUser3.getId());

        Assertions.assertThat(friendDao.getCommonFriends(createdTestUser.getId(), createdTestUser3.getId()).getFirst())
                .isEqualTo(createdTestUser2);
    }
}
