package ru.yandex.practicum.filmorate.mapper;

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

    UserDTO usersToUserDTO(User user);

    @Mapping(target = "name", expression = "java(mapName(userDTO))")
    User userDtoToUser(UserDTO userDTO);

    List<UserDTO> usersToUserDTO(Collection<User> users);

    User updateUser(@MappingTarget User user1, UserDTO user2);

    default String mapName(UserDTO userDTO) {
        return userDTO.getName() == null ? userDTO.getLogin() : userDTO.getName();
    }
}
