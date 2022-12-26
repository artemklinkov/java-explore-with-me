package ru.practicum.explore.dto.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.model.request.Request;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getState())
                .created(request.getCreatedOn()).build();
    }
}
