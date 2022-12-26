package ru.practicum.explore.service.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explore.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findUsers(List<Long> ids, PageRequest pageRequest);

    UserDto registerUser(String email, String name);

    void deleteUser(Long userId);
}
