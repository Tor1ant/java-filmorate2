package ru.yandex.practicum.filmorate.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.generated.model.dto.UserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {


    @Mapping(target = "name", expression = "java(mapName(userDTO))")
    @Mapping(target = "birthday", expression = "java(mapBirthday(userDTO))")
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);

    List<UserDTO> toDto(Collection<User> users);

    User updateUser(@MappingTarget User user1, UserDTO user2);

    default String mapName(UserDTO userDTO) {
        return userDTO.getName() == null ? userDTO.getLogin() : userDTO.getName();
    }

    default LocalDate mapBirthday(UserDTO userDTO) {
        return LocalDate.parse(userDTO.getBirthday());
    }
}
