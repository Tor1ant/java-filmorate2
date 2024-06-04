package ru.yandex.practicum.filmorate.storage.dao.mapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.film.Film;
import ru.yandex.practicum.filmorate.model.entity.film.enumerated.MPA;

@Component
public class FilmRowMapper implements RowMapper<Film> {


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Long> likes = new HashSet<>();
        Array likesArray = rs.getArray("likes");
        if (likesArray != null) {
            ResultSet likesResultSet = likesArray.getResultSet();
            while (likesResultSet.next()) {
                likes.add(likesResultSet.getLong("VALUE"));
            }
        }
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(MPA.fromId(rs.getInt("mpa_id")))
                .likes(likes)
                .build();

    }
}
