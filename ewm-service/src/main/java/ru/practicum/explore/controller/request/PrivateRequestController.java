package ru.practicum.explore.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.service.request.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Private API to work with user requests to take part in events
 */

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> findAllRequests(@PathVariable Long userId,
                                                         HttpServletRequest request) {
        log.info("{}: {}; find all user requests with id = {}", request.getRequestURI(),
                request.getRemoteAddr(), userId);
        return requestService.findAllUserRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam Long eventId,
                                              HttpServletRequest request) {
        log.info("{}: {}; add request for the event with id = {} from user with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), eventId, userId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId,
                                                 HttpServletRequest request) {
        log.info("{}: {}; cancel request for the event with id = {} from user with id = {}",
                request.getRemoteAddr(), request.getRequestURI(), requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }
}
