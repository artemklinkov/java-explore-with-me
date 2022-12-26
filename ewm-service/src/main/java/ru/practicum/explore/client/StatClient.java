package ru.practicum.explore.client;

import ru.practicum.explore.dto.stats.EndpointHit;
import ru.practicum.explore.dto.stats.ViewStats;

import java.util.List;

public interface StatClient {

    void createHit(EndpointHit endpointHit);

    /**
     * Get statistics by views
     *
     * @param start  range's starting date and time
     * @param end    range's ending date and time
     * @param uris   list of URIs for which the statistics should be loaded
     * @param unique if views with unique IP should be found
     */
    List<ViewStats> findStats(String start, String end, List<String> uris, Boolean unique);
}
