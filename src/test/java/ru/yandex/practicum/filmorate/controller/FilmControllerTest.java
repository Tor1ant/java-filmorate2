package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.entity.Film;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final FilmMapper filmMapper = Mappers.getMapper(FilmMapper.class);

    private final Film film = Film.builder()
            .name("А зори здесь тихие")
            .description(
                    "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                    + "самоотверженных девушек-зенитчиц и их командира")
            .releaseDate(LocalDate.of(1972, 1, 1))
            .duration(160L)
            .build();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FilmController(filmMapper))
                .build();
    }


    @Test
    @DisplayName("Проверка создания фильма")
    void createFilm() throws Exception {
        Film createdFilm = Film.builder()
                .id(1L)
                .name("А зори здесь тихие")
                .description(
                        "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                        + "самоотверженных девушек-зенитчиц и их командира")
                .releaseDate(LocalDate.of(1972, 1, 1))
                .duration(160L)
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(createdFilm)));
    }

    @Test
    @DisplayName("Проверка создания фильма с пустым названием")
    void createFilmWithEmptyName() throws Exception {
        film.setName(null);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания фильма с пустым описанием")
    void createFilmWithEmptyDescription() throws Exception {
        film.setDescription(null);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания фильма с слишком длинным описанием")
    void createFilmWithTooLongDescription() throws Exception {
        String badDescription = RandomStringUtils.randomAlphabetic(251);
        film.setDescription(badDescription);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания фильма в будущем")
    void createFilmWithFutureReleaseDate() {
        film.setReleaseDate(LocalDate.now().plusDays(1));
        Assertions.assertThatThrownBy(() -> mockMvc.perform(post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                        .andExpect(status().is5xxServerError()))
                .isInstanceOf(ServletException.class);
    }

    @Test
    @DisplayName("Проверка создания фильма с отрицательной длительностью")
    void createFilmWithNegativeDuration() throws Exception {
        film.setDuration(-1L);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания фильма с нулевой длительностью")
    void createFilmWithZeroDuration() throws Exception {
        film.setDuration(0L);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка создания фильма с датой релиза меньше даты создания кино")
    void createFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1870, 12, 28));
        Assertions.assertThatThrownBy(() -> mockMvc.perform(post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType("application/json"))
                        .andExpect(status().is5xxServerError()))
                .isInstanceOf(ServletException.class);
    }

    @Test
    @DisplayName("Проверка обновления фильма")
    void updateFilm() throws Exception {
        Film updatedFilm = Film.builder()
                .id(1L)
                .name("А зори здесь тихие")
                .description(
                        "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                        + "самоотверженных девушек-зенитчиц и их командира")
                .releaseDate(LocalDate.of(2015, 1, 1))
                .duration(160L)
                .build();

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(updatedFilm))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedFilm)));
    }

    @Test
    @DisplayName("Проверка получения фильма по id")
    void getFilmById() throws Exception {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        film.setId(1L);
        mockMvc.perform(get(String.format("/films/%s", film.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));

    }

    @Test
    @DisplayName("Проверка получения списка фильмов")
    void getFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().is2xxSuccessful());
    }
}