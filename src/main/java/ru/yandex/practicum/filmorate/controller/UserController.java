package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.generated.api.UsersApi;
import ru.yandex.practicum.generated.model.dto.UserDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final Map<Long, User> users = new HashMap<>();
    private final UserMapper mapper;
    private Long id = 0L;

    @Override
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        log.info("Поступил запрос на создание пользователя {}", userDTO);
        validate(userDTO);
        User user = mapper.userDtoToUser(userDTO);
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Пользователь {} создан", user);
        return ResponseEntity.status(201).body(mapper.usersToUserDTO(user));
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(UserDTO userDTO) {
        log.info("Поступил запрос на обновление пользователя {}", userDTO);
        validate(userDTO);
        User updatedUser = users.compute(userDTO.getId(),
                (k, userForUpdate) -> mapper.updateUser(userForUpdate, userDTO));
        log.debug("Пользователь {} обновлен", updatedUser);
        return ResponseEntity.ok(mapper.usersToUserDTO(updatedUser));
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return ResponseEntity.ok(mapper.usersToUserDTO(users.get(id)));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> userDTOS = mapper.usersToUserDTO(users.values());
        return ResponseEntity.ok(userDTOS);
    }


    private void validate(UserDTO userDTO) {
        LocalDate now = LocalDate.now();
        if (userDTO.getBirthday().isAfter(now)) {
            log.error("Дата рождения не может быть больше текущей. Дата рождения: {}, текущая дата: {}",
                    userDTO.getBirthday(), now);
            throw new ValidationException("Дата рождения не может быть больше текущей");
        }
    }
}
