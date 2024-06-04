package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.entity.User;

public interface UserStorage {

    User create(User user);

    User update(User user);

    User getById(Long id);

    User delete(Long id);

    List<User> getAll();
}
