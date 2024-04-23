package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.entity.User;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final User user = User.builder()
            .login("login")
            .name("Name name")
            .email("test@mail.ru")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userMapper))
                .build();
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void createUser() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Проверка создания пользователя с пустым логином")
    void createUserWithEmptyNullLogin() throws Exception {
        user.setLogin(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с логином, содержащим пробелы")
    void createUserWithLoginWithSpaces() throws Exception {
        user.setLogin("login with spaces");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с пустым логином")
    void createUserWithEmptyLogin() throws Exception {
        user.setLogin("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с логином только из пробелов")
    void createUserWithLoginOnlySpaces() throws Exception {
        user.setLogin("  ");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с пустым email")
    void createUserWithEmptyEmail() throws Exception {
        user.setEmail(null);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с некорректным email")
    void createUserWithIncorrectEmail() throws Exception {
        user.setEmail("testmail");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания пользователя с некорректной датой рождения")
    void createUserWithIncorrectBirthday() {
        user.setBirthday(LocalDate.now().plusYears(2));
        Assertions.assertThatThrownBy(() -> mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is5xxServerError())).isInstanceOf(ServletException.class);
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    void updateUser() throws Exception {
        User updatedUser = User.builder()
                .id(1L)
                .login("newLogin")
                .name("new name")
                .email("test@mail.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(updatedUser))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    @DisplayName("Проверка получения пользователя по id")
    void getUserById() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Проверка получения списка пользователей")
    void getUsers() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful());
    }
}