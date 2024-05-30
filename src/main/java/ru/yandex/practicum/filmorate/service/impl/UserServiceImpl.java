package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.generated.model.dto.UserDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper mapper;
    private final FriendStorage friendStorage;

    @Override
    public UserDTO create(UserDTO user) {
        log.info("Запрос на создание пользователя: {}", user);
        User userToCreate = mapper.toEntity(user);
        userToCreate = userStorage.create(userToCreate);
        log.info("Пользователь создан: {}", userToCreate);
        return mapper.toDto(userToCreate);
    }

    @Override
    public UserDTO update(UserDTO user) {
        log.info("Запрос на обновление пользователя: {}", user);
        User userForUpdate = getEntityById(user.getId());
        User updatedUser = mapper.updateUser(userForUpdate, user);
        updatedUser = userStorage.update(updatedUser);
        log.info("Пользователь обновлен: {}", updatedUser);
        return mapper.toDto(updatedUser);
    }

    @Override
    public UserDTO getById(Long id) {
        log.info("Запрос на получение пользователя по id: {}", id);
        User user = getEntityById(id);
        log.info("Пользователь получен: {}", user);
        return mapper.toDto(user);
    }

    @Override
    public UserDTO delete(Long id) {
        log.info("Запрос на удаление пользователя по id: {}", id);
        User deletedUser = userStorage.delete(id);
        return mapper.toDto(deletedUser);
    }

    @Override
    public List<UserDTO> getAll() {
        log.info("Запрос на получение пользователей");
        List<User> all = userStorage.getAll();
        log.info("Получены все пользователи");
        return mapper.toDto(all);
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        log.info("Запрос от пользователя с id={} на добавление в друзья пользователя с id={}", id, friendId);
        boolean isAdded = friendStorage.addFriendRequest(id, friendId);
        log.debug("Результат добавления в друзья пользователя с id={} и пользователя с id={}: {}", id, friendId,
                isAdded);
        return isAdded;

    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        log.info("Запрос от пользователя с id={} на удаления друга с id={}", id, friendId);
        boolean isDeleted = friendStorage.deleteFriendRequest(id, friendId);
        log.debug("Друг с id={} удален={} из друзей пользователя с id={}", friendId, id, isDeleted);
        return isDeleted;
    }

    @Override
    public List<UserDTO> getFriends(Long id) {
        log.info("Запрос от пользователя с id={} на получение друзей", id);
        List<UserDTO> dtos = mapper.toDto(friendStorage.getFriends(id));
        log.debug("Друзья пользователя с id={} получены: {}", id, dtos);
        return dtos;
    }

    @Override
    public List<UserDTO> getCommonFriends(Long id, Long otherId) {
        log.info("Запрос от пользователя с id={} на получение общих друзей пользователя с id={}", id, otherId);
        List<UserDTO> commonFriends = mapper.toDto(friendStorage.getCommonFriends(id, otherId));
        log.info("Общие друзья пользователя с id={} и пользователя с id={} получены: {}", id, otherId, commonFriends);
        return commonFriends;
    }

    private User getEntityById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException(String.format("пользователь с id=%s не найден", id));
        }
        return user;
    }
}
