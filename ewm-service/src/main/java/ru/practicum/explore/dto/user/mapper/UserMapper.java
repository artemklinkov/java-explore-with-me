package ru.practicum.explore.dto.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.model.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
