package ru.yandex.practicum.filmorate.model.entity;

import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
public class User {

    @Id
    private Long id;
    private String email;
    private String login;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public Map<String, Object> toMap() {
        return Map.of(
                "email", email,
                "login", login,
                "name", name,
                "birthday", birthday
        );
    }
}
