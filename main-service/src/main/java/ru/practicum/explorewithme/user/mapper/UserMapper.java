package ru.practicum.explorewithme.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;
import ru.practicum.explorewithme.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    UserShortDto toShortDto(User user);

    List<UserDto> toDtoList(List<User> users);
}

