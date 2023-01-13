package ru.practicum.explore.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.dto.request.mapper.RequestMapper;
import ru.practicum.explore.handler.exception.AccessLevelException;
import ru.practicum.explore.handler.exception.ConflictException;
import ru.practicum.explore.handler.exception.NotAvailableException;
import ru.practicum.explore.handler.exception.NotFoundException;
import ru.practicum.explore.model.User;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestState;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;
import ru.practicum.explore.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final EventService eventService;


    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Event event = validateEvent(eventId);
        User user = validateUser(userId);
        requestRepository.findByEventIdAndRequesterId(eventId, userId)
                .ifPresent((r) -> {
                    throw new ConflictException("User is already a participant of the given event");
                });

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new AccessLevelException("The initiator of the event cannot add " +
                    "a request to participate in his own event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new AccessLevelException("User cannot participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0 &&
                Objects.equals(requestRepository.countByEventIdAndState(eventId, RequestState.CONFIRMED),
                        event.getParticipantLimit())) {
            throw new NotAvailableException("The event has reached the participant limit of requests");
        }
        RequestState state;
        if (event.getRequestModeration()) {
            state = RequestState.PENDING;
        } else {
            state = RequestState.CONFIRMED;
        }
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(user);
        request.setState(state);
        request.setCreatedOn(LocalDateTime.now());

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findAllUserRequests(Long userId) {
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = validateRequest(requestId);
        validateUser(userId);

        if (request.getState() == RequestState.CANCELED) {
            throw new NotAvailableException("This request has already been canceled");
        }
        request.setState(RequestState.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        EventFullDto event = eventService.findUserEventById(userId, eventId);
        if (event.getConfirmedRequests() == null) {
            event.setConfirmedRequests(0L);
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() &&
                event.getParticipantLimit() != 0) {
            throw new NotAvailableException("The event 0" +
                    "has reached the participant limit of requests");
        }
        Request request = validateRequest(reqId);
        request.setState(RequestState.CONFIRMED);
        eventService.updateCountOfConfirmedParticipants(eventId);

        if (event.getConfirmedRequests() == event.getParticipantLimit() - 1) {
            rejectAllPendingEventRequests(eventId);
        }
        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        Request request = validateRequest(reqId);
        validateUser(userId);

        request.setState(RequestState.REJECTED);
        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> findEventRequests(Long userId, Long eventId) {
        return requestRepository.findByEventIdAndEventInitiatorId(eventId, userId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void rejectAllPendingEventRequests(Long eventId) {
        Event event = validateEvent(eventId);
        if (event.getConfirmedRequests() == event.getParticipantLimit() - 1) {
            requestRepository.rejectPendingRequests(eventId);
        }
    }

    private Request validateRequest(Long reqId) {
        return requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("Request is not found"));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
    }

    private Event validateEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found"));
    }
}
