package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.dto.mapper.HitMapper;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final HitMapper hitMapper;
    private final DateTimeDecoder decoder;

    @Override
    public EndpointHit createHit(EndpointHit endpointHit) {
        Hit hit = statRepository.save(hitMapper.toHit(endpointHit));
        return hitMapper.toEndpointHit(hit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startTime = decoder.dateEncoder(start);
        LocalDateTime endTime = decoder.dateEncoder(end);
        if (unique) {
            return statRepository.calculateUniqueStats(uris, startTime, endTime);
        }
        return statRepository.calculateStats(uris, startTime, endTime);
    }
}
