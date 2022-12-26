package ru.practicum.explore.service;

import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;

import java.util.List;

public interface StatService {

    EndpointHit createHit(EndpointHit endpointHit);

    List<ViewStats> findStats(String start, String end, List<String> uris, boolean unique);
}
