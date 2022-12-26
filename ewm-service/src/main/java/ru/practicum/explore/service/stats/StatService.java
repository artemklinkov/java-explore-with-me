package ru.practicum.explore.service.stats;

import ru.practicum.explore.dto.stats.ViewStats;

import java.util.List;

public interface StatService {

    List<ViewStats> getViewStats(List<String> uris);

    void sendHit(String uri, String ip);
}
