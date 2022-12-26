package ru.practicum.explore.service.event;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explore.dto.event.*;
import ru.practicum.explore.model.event.EventSortingType;
import ru.practicum.explore.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    // Admin access level
    EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request);

    List<EventFullDto> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    // Authorized user access level
    List<EventShortDto> findEventsByUserId(Long userId, PageRequest pageRequest);

    EventFullDto updateEventByUser(Long userId, UpdateEventRequest request);

    EventFullDto createEventByUser(Long userId, NewEventDto request);

    EventFullDto findUserEventById(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    // All users access level
    List<EventShortDto> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortingType sort,
                                   PageRequest pageRequest);

    EventFullDto findEventById(Long eventId);

    void updateCountOfConfirmedParticipants(Long eventId);
}
