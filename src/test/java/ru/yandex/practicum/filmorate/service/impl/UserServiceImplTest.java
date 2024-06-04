package ru.yandex.practicum.filmorate.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.generated.model.dto.UserDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserStorage userStorage;
    @Mock
    private FriendStorage friendStorage;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private UserServiceImpl userService;

    private UserDTO userDto;
    private UserDTO userDtoAfterCreate;
    private User userBeforeCreate;
    private User createdUser;
    private UserDTO secondUserDto;
    private User secondUser;

    @BeforeEach
    void setUp() {
        userDto = new UserDTO()
                .id(null)
                .login("login")
                .email("Test@mail.ru")
                .name("name")
                .birthday("2000-01-01");
        userDtoAfterCreate = new UserDTO()
                .id(1L)
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .birthday(userDto.getBirthday());
        userBeforeCreate = User.builder()
                .id(null)
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .birthday(LocalDate.parse(userDto.getBirthday()))
                .build();
        createdUser = User.builder()
                .id(1L)
                .login(userDto.getLogin())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .birthday(LocalDate.parse(userDto.getBirthday()))
                .build();
        secondUserDto = new UserDTO()
                .id(2L)
                .login("tgfgfd")
                .name("Алексей")
                .email("mail@mail.ru")
                .birthday("2000-01-01");

        secondUser = User.builder()
                .id(2L)
                .login(secondUserDto.getLogin())
                .email(secondUserDto.getEmail())
                .name(secondUserDto.getName())
                .birthday(LocalDate.parse(secondUserDto.getBirthday()))
                .build();

        userService = new UserServiceImpl(userStorage, userMapper, friendStorage);
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void create() {
        Mockito.when(userStorage.create(userBeforeCreate))
                .thenReturn(createdUser);

        UserDTO result = userService.create(userDto);

        Assertions.assertThat(result).isEqualTo(userDtoAfterCreate);
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    void update() {
        String newMail = "newMail@mail.ru";
        userDtoAfterCreate.setEmail(newMail);

        User userForUpdate = User.builder()
                .id(1L)
                .login(userDto.getLogin())
                .email(newMail)
                .name(userDto.getName())
                .birthday(LocalDate.parse(userDto.getBirthday()))
                .build();

        Mockito.when(userStorage.getById(1L))
                .thenReturn(createdUser);
        Mockito.when(userStorage.update(userForUpdate))
                .thenReturn(userForUpdate);

        UserDTO result = userService.update(userDtoAfterCreate);

        Assertions.assertThat(result).isEqualTo(userDtoAfterCreate);

    }

    @Test
    @DisplayName("Проверка получения пользователя по id")
    void getById() {
        Mockito.when(userStorage.getById(1L))
                .thenReturn(createdUser);

        UserDTO result = userService.getById(1L);

        Assertions.assertThat(result).isEqualTo(userDtoAfterCreate);
    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    void delete() {
        Mockito.when(userStorage.delete(1L)).thenReturn(createdUser);

        UserDTO result = userService.delete(1L);

        Assertions.assertThat(result).isEqualTo(userDtoAfterCreate);
    }

    @Test
    @DisplayName("Проверка получения всех пользователей")
    void getAll() {
        Mockito.when(userStorage.getAll())
                .thenReturn(List.of(createdUser));

        List<UserDTO> result = userService.getAll();

        Assertions.assertThat(result).isEqualTo(List.of(userDtoAfterCreate));
    }

    @Test
    @DisplayName("Проверка добавления друга")
    void addFriend() {
        Mockito.when(friendStorage.addFriendRequest(1L, 2L)).thenReturn(true);

        boolean added = userService.addFriend(1L, 2L);

        Assertions.assertThat(added).isTrue();
    }

    @Test
    @DisplayName("Проверка удаления друга")
    void deleteFriend() {
        Mockito.when(friendStorage.deleteFriendRequest(1L, 2L)).thenReturn(true);

        userService.deleteFriend(1L, 2L);

        Assertions.assertThat(friendStorage.deleteFriendRequest(1L, 2L)).isTrue();
    }

    @Test
    @DisplayName("Проверка получения друзей")
    void getFriends() {
        Mockito.when(friendStorage.getFriends(1L)).thenReturn(List.of(createdUser));

        List<UserDTO> result = userService.getFriends(1L);

        Assertions.assertThat(result.getFirst()).isEqualTo(userDtoAfterCreate);
    }

    @Test
    @DisplayName("Проверка получения общих друзей")
    void getCommonFriends() {
        Mockito.when(friendStorage.getCommonFriends(1L, 2L)).thenReturn(List.of(secondUser));

        List<UserDTO> commonFriends = userService.getCommonFriends(1L, 2L);

        Assertions.assertThat(commonFriends).contains(secondUserDto);
    }
}
