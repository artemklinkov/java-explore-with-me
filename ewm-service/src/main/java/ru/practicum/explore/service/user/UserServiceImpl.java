package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.user.mapper.UserMapper;
import ru.practicum.explore.handler.exception.ConflictException;
import ru.practicum.explore.model.User;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findUsers(List<Long> ids, PageRequest pageRequest) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageRequest)
                    .getContent()
                    .stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findByIdIn(ids, pageRequest)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto registerUser(String email, String name) {
        userRepository.findByName(name).ifPresent(s -> {
            throw new ConflictException("User with provided name already exists.");
        });
        userRepository.findByEmail(email).ifPresent(s -> {
            throw new ConflictException("User with provided email already exists.");
        });
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
