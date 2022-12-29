package ru.practicum.explore.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.model.event.EventSortingType;
import ru.practicum.explore.service.event.EventService;
import ru.practicum.explore.service.stats.StatService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

/**
 * Public API to work with events
 */

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    private final EventService eventService;
    private final StatService statService;

    @GetMapping
    public List<EventShortDto> findEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSortingType sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("{}: {}; find events", request.getRequestURI(), request.getRemoteAddr());

        statService.sendHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, PageRequest.of(from / size, size));
    }

    @GetMapping("/{id}")
    public EventFullDto findEventById(@PathVariable Long id,
                                      HttpServletRequest request) {
        log.info("{}: {}; find event with id = {}", request.getRequestURI(), request.getRemoteAddr(), id);
        statService.sendHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.findEventById(id);
    }
}
