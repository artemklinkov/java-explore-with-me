package ru.practicum.explore.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event.*;
import ru.practicum.explore.dto.event.mapper.EventMapper;
import ru.practicum.explore.dto.stats.ViewStats;
import ru.practicum.explore.handler.exception.AccessLevelException;
import ru.practicum.explore.handler.exception.IncorrectEventTimeException;
import ru.practicum.explore.handler.exception.NotAvailableException;
import ru.practicum.explore.handler.exception.NotFoundException;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.User;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventSortingType;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.stats.StatService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final StatService statService;
    private static final int HOURS = 2;

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request) {
        Event event = validateEvent(eventId);
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getCategory() != null) {
            Category category = validateCategory(request.getCategory());
            event.setCategory(category);
        }
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public List<EventFullDto> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest) {
        return eventRepository.findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, pageRequest)
                .stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = validateEvent(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new NotAvailableException("Event should be in a pending state");
        }
        LocalDateTime dateOfPublication = LocalDateTime.now();
        LocalDateTime limit = dateOfPublication.plusHours(HOURS);

        if (event.getEventDate().isBefore(limit)) {
            throw new IncorrectEventTimeException("Too little time before the start of the current event");
        }
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(dateOfPublication);
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = validateEvent(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new NotAvailableException("Incorrect state. Event has already been published");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsByUserId(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest)
                .stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, UpdateEventRequest request) {
        Event event = validateEvent(request.getEventId());
        User requester = validateUser(userId);

        if (!event.getInitiator().equals(requester)) {
            throw new AccessLevelException("User should be an initiator of the event");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new NotAvailableException("Incorrect state. Event has already been published");
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            Category category = validateCategory(request.getCategory());
            event.setCategory(category);
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(HOURS))) {
                throw new IncorrectEventTimeException("Too little time before the start of the current event");
            }
            event.setEventDate(request.getEventDate());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto createEventByUser(Long userId, NewEventDto request) {
        User initiator = validateUser(userId);
        Category category = validateCategory(request.getCategory());

        LocalDateTime dateOfPublication = LocalDateTime.now();
        LocalDateTime limit = dateOfPublication.plusHours(HOURS);
        if (request.getEventDate().isBefore(limit)) {
            throw new IncorrectEventTimeException("Too little time before the start of the current event");
        }
        Event event = eventMapper.toEvent(request);
        event.setState(EventState.PENDING);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto findUserEventById(Long userId, Long eventId) {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        User user = validateUser(userId);
        if (optEvent.isPresent() && !user.equals(optEvent.get().getInitiator())) {
            throw new AccessLevelException("User should be an initiator of the event");
        }
        return eventMapper.toFullDto(optEvent.orElseThrow(() -> new NotFoundException("Event is not found")));
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = validateEvent(eventId);
        User requester = validateUser(userId);
        if (!event.getInitiator().equals(requester)) {
            throw new AccessLevelException("User should be an initiator of the event");
        }
        if (event.getState() != EventState.PENDING) {
            throw new NotAvailableException("Event should be in a pending state");
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public List<EventShortDto> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortingType sortingType,
                                          PageRequest pageRequest) {
        if (onlyAvailable == null) {
            onlyAvailable = false;
        }
        if (sortingType == null) {
            sortingType = EventSortingType.EVENT_DATE;
        }
        switch (sortingType) {
            case EVENT_DATE:
                Sort sort = Sort.sort(Event.class).by(Event::getEventDate).ascending();

                List<Event> sortedEvents = eventRepository.findEvents(text, categories, paid, rangeStart, rangeEnd,
                        pageRequest.withSort(sort));
                if (onlyAvailable) {
                    sortedEvents = sortedEvents.stream().filter(e -> e.getConfirmedRequests() < e.getParticipantLimit()
                            || e.getParticipantLimit() == 0).collect(Collectors.toList());
                }
                return sortedEvents.stream().map(eventMapper::toShortDto).collect(Collectors.toList());
            case VIEWS:
                List<Event> events = eventRepository.findEvents(text, categories, paid, rangeStart, rangeEnd,
                        pageRequest);
                if (onlyAvailable) {
                    events = events.stream().filter(e -> e.getConfirmedRequests() < e.getParticipantLimit()
                            || e.getParticipantLimit() == 0).collect(Collectors.toList());
                }
                return findViewsFromStatistics(events)
                        .stream()
                        .sorted(Comparator.comparing(EventShortDto::getViews))
                        .collect(Collectors.toList());
            default:
                throw new NotFoundException("Sorting type is incorrect.");
        }
    }

    @Override
    public EventFullDto findEventById(Long eventId) {
        return eventMapper.toFullDto(validateEvent(eventId));
    }

    @Override
    public void updateCountOfConfirmedParticipants(Long eventId) {
        Event event = validateEvent(eventId);
        if (event.getConfirmedRequests() == null) {
            event.setConfirmedRequests(1L);
            eventRepository.save(event);
            return;
        }
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    private List<EventShortDto> findViewsFromStatistics(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();
        List<String> uris = events.stream().map(e -> "event/" + e.getId()).collect(Collectors.toList());
        List<ViewStats> viewStats = statService.getViewStats(uris);

        for (ViewStats viewStat : viewStats) {
            String[] uri = viewStat.getUri().split("/");
            long id = Long.parseLong(uri[uri.length - 1]);
            views.put(id, viewStat.getHits());
        }
        List<EventShortDto> shortDtos = events.stream().map(eventMapper::toShortDto).collect(Collectors.toList());
        for (EventShortDto dto : shortDtos) {
            dto.setViews(views.get(dto.getId()));
        }
        return shortDtos;
    }

    private Category validateCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category is not found"));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
    }

    public Event validateEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found"));
    }
}
