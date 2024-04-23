
package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.generated.api.UsersApi;
import ru.yandex.practicum.generated.model.dto.UserDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        log.info("Поступил запрос на создание пользователя {}", userDTO);
        validate(userDTO);
        return ResponseEntity.status(201).body(userService.create(userDTO));
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(UserDTO user) {
        log.info("Поступил запрос на обновление пользователя {}", user);
        validate(user);

        return ResponseEntity.ok(userService.update(user));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    public ResponseEntity<Boolean> addFriend(Long id, Long friendId) {
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @Override
    public ResponseEntity<Boolean> deleteFriend(Long id, Long friendId) {
        return ResponseEntity.ok(userService.deleteFriend(id, friendId));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getFriends(Long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getCommonFriends(Long id, Long otherId) {
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }

    private void validate(UserDTO user) {
        LocalDate now = LocalDate.now();
        LocalDate userBirthday = LocalDate.parse(user.getBirthday());
        if (userBirthday.isAfter(now)) {
            log.error("Дата рождения не может быть больше текущей. Дата рождения: {}, текущая дата: {}",
                    user.getBirthday(), now);
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }
    }
}

