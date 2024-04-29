package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.generated.model.dto.UserDTO;

public interface UserService {

    UserDTO create(UserDTO user);

    UserDTO update(UserDTO user);

    UserDTO getById(Long id);

    UserDTO delete(Long id);

    List<UserDTO> getAll();

    /**
     * Добавление в друзья происходит взаимно. То есть если один пользователь добавил в друзья другого, то второй
     * автоматически добавит в друзья первого
     *
     * @param id id пользователя
     * @param friendId id друга
     * @return true если добавление прошло успешно
     */
    boolean addFriend(Long id, Long friendId);

    boolean deleteFriend(Long id, Long friendId);

    List<UserDTO> getFriends(Long id);

    List<UserDTO> getCommonFriends(Long id, Long otherId);
}
