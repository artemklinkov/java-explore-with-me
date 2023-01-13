package ru.practicum.explore.service.request;

import ru.practicum.explore.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> findAllUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId);

    List<ParticipationRequestDto> findEventRequests(Long userId, Long eventId);

    void rejectAllPendingEventRequests(Long eventId);
}
