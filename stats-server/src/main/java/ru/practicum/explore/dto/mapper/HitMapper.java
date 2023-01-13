package ru.practicum.explore.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.service.DateTimeDecoder;

@Component
@RequiredArgsConstructor
public class HitMapper {

    private final DateTimeDecoder decoder;

    public Hit toHit(EndpointHit endpointHit) {
        return new Hit(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                decoder.dateEncoder(endpointHit.getTimestamp())
        );
    }

    public EndpointHit toEndpointHit(Hit hit) {
        return new EndpointHit(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().toString()
        );
    }
}
