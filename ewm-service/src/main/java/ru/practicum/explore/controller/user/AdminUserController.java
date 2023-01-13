package ru.practicum.explore.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * API for admin to work with users
 */

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                   HttpServletRequest request) {
        log.info("{}: {}; find users", request.getRequestURI(), request.getRemoteAddr());
        return userService.findUsers(ids, PageRequest.of(from / size, size));
    }

    @PostMapping
    public UserDto registerUser(@RequestBody @Valid NewUserRequest userRequest,
                                HttpServletRequest request) {
        log.info("{}: {}; register user {}", request.getRequestURI(),
                request.getRemoteAddr(), userRequest.toString());
        return userService.registerUser(userRequest.getEmail(), userRequest.getName());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId,
                           HttpServletRequest request) {
        log.info("{}: {}; delete user with id = {}", request.getRequestURI(), request.getRemoteAddr(), userId);
        userService.deleteUser(userId);
    }
}
