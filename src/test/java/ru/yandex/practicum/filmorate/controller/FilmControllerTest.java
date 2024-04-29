
package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.handler.Handler;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.generated.model.dto.FilmDTO;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FilmService filmService;

    private final FilmDTO film = new FilmDTO()
            .name("А зори здесь тихие")
            .description(
                    "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                    + "самоотверженных девушек-зенитчиц и их командира")
            .releaseDate("1972-01-01")
            .duration(160L);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FilmController(filmService))
                .setControllerAdvice(new Handler())
                .build();
    }


    @Test
    @DisplayName("Проверка создания фильма")
    void createFilm() throws Exception {
        FilmDTO createdFilm = new FilmDTO()
                .id(1L)
                .name("А зори здесь тихие")
                .description(
                        "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                        + "самоотверженных девушек-зенитчиц и их командира")
                .releaseDate("1972-01-01")
                .duration(160L);

        Mockito.when(filmService.create(Mockito.any())).thenReturn(createdFilm);

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
    void createFilmWithFutureReleaseDate() throws Exception {
        film.setReleaseDate(LocalDate.now().plusDays(1).toString());
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
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
    void createFilmWithWrongReleaseDate() throws Exception {
        film.setReleaseDate(LocalDate.of(1870, 12, 28).toString());
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Проверка обновления фильма")
    void updateFilm() throws Exception {
        FilmDTO updatedFilm = new FilmDTO()
                .id(1L)
                .name("А зори здесь тихие")
                .description(
                        "художественное произведение, написанное Борисом Васильевым, повествующее о судьбах пяти "
                        + "самоотверженных девушек-зенитчиц и их командира")
                .releaseDate(LocalDate.of(2015, 1, 1).toString())
                .duration(160L);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful());

        Mockito.when(filmService.update(Mockito.any())).thenReturn(updatedFilm);

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

        Mockito.when(filmService.getById(1L)).thenReturn(film);

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
