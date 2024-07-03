package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.entity.User;

public interface FriendStorage {

    boolean addFriendRequest(Long id, Long friendId);

    boolean deleteFriendRequest(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long friendId);
}
