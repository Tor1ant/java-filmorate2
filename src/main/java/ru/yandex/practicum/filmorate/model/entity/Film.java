package ru.yandex.practicum.filmorate.model.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private final Set<Long> likes = new HashSet<>();
}
