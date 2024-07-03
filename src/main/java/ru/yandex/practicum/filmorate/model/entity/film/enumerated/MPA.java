package ru.yandex.practicum.filmorate.model.entity.film.enumerated;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Getter
public enum MPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final int id;
    private final String value;

    MPA(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public static MPA fromId(int id) {
        return switch (id) {
            case 1 -> G;
            case 2 -> PG;
            case 3 -> PG_13;
            case 4 -> R;
            case 5 -> NC_17;
            default -> throw new ValidationException("Неизвестный рейтинг");
        };
    }

    @Override
    public String toString() {
        return "{\"id\": " + id + ", \"name\": \"" + name() + "\"}";
    }
}
