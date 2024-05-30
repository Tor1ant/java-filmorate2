package ru.yandex.practicum.filmorate.model.entity.film.enumerated;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Getter
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final int id;
    private final String ruName;

    Genre(int id, String ruName) {
        this.id = id;
        this.ruName = ruName;
    }

    public static Genre fromId(int id) {
        return switch (id) {
            case 1 -> COMEDY;
            case 2 -> DRAMA;
            case 3 -> CARTOON;
            case 4 -> THRILLER;
            case 5 -> DOCUMENTARY;
            case 6 -> ACTION;
            default -> throw new ValidationException("Неизвестный жанр c id " + id);
        };
    }

    @Override
    public String toString() {
        return "{\"id\": " + id + ", \"name\": \"" + name() + "\"}";
    }
}
