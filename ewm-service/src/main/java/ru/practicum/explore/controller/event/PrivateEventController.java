package ru.practicum.explore.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateEventRequest;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.service.event.EventService;
import ru.practicum.explore.service.request.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Private API to work with events
 */

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<EventShortDto> findEventsByUserId(@PathVariable Long userId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size,
                                                  HttpServletRequest request) {
        log.info("{}: {};  user with id = {} to find {} events, from {}",
                request.getRequestURI(), request.getRemoteAddr(), userId, size, from);
        return eventService.findEventsByUserId(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @Valid @RequestBody UpdateEventRequest updRequest,
                                          HttpServletRequest request) {
        log.info("{}: {};  user with id = {} to update {}", request.getRequestURI(), request.getRemoteAddr(),
                userId, updRequest);
        return eventService.updateEventByUser(userId, updRequest);
    }

    @PostMapping
    public EventFullDto createEventByUser(@PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto eventDto,
                                          HttpServletRequest request) {
        log.info("{}: {}; create event {} by user with id = {}", request.getRemoteAddr(),
                request.getRequestURI(), eventDto.toString(), userId);
        return eventService.createEventByUser(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventById(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      HttpServletRequest request) {
        log.info("{}: {}; find event with id = {}", request.getRequestURI(), request.getRemoteAddr(), eventId);
        return eventService.findUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    HttpServletRequest request) {
        log.info("{}: {}; cancel event with id = {}", request.getRequestURI(), request.getRemoteAddr(), eventId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findEventRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId,
                                                           HttpServletRequest request) {
        log.info("{}: {};  user with id ={} to find requests for the event with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), userId, eventId);
        return requestService.findEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId,
                                                  HttpServletRequest request) {
        log.info("{}: {}; confirm request with id = {} for the event with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), reqId, eventId);
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId,
                                                 HttpServletRequest request) {
        log.info("{}: {}; reject request with id = {} for the event with id = {} ",
                request.getRequestURI(), request.getRemoteAddr(), reqId, eventId);
        return requestService.rejectRequest(userId, eventId, reqId);
    }
}
