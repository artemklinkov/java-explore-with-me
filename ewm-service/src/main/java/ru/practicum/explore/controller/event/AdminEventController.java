package ru.practicum.explore.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.AdminUpdateEventRequest;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

/**
 * API for admin to work with events
 */

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEventsByAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("{}: {}; find events by admin", request.getRequestURI(), request.getRemoteAddr());
        return eventService.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid AdminUpdateEventRequest updRequest,
                                           HttpServletRequest request) {
        log.info("{}: {}; update event by admin with id = {}", request.getRequestURI(), request.getRemoteAddr(),
                eventId);
        return eventService.updateEventByAdmin(eventId, updRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId,
                                     HttpServletRequest request) {
        log.info("{}: {}; publish event with id = {}", request.getRequestURI(), request.getRemoteAddr(), eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId,
                                    HttpServletRequest request) {
        log.info("{}: {}; reject event with id = {}", request.getRequestURI(), request.getRemoteAddr(), eventId);
        return eventService.rejectEvent(eventId);
    }
}
