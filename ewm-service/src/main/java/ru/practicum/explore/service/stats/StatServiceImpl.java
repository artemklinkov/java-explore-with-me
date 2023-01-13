package ru.practicum.explore.service.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.StatClientImpl;
import ru.practicum.explore.dto.stats.EndpointHit;
import ru.practicum.explore.dto.stats.ViewStats;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    @Value("${ewm.service.name}")
    private String serviceName;
    private final StatClientImpl statClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    @Override
    public List<ViewStats> getViewStats(List<String> uris) {
        String start = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC)
                .format(formatter);
        String end = LocalDateTime.now().plusYears(50).format(formatter);
        return statClient.findStats(start, end, uris, false);
    }

    @Override
    public void sendHit(String uri, String ip) {
        EndpointHit endpointHit = new EndpointHit(null, serviceName, uri, ip,
                LocalDateTime.now().format(formatter));
        statClient.createHit(endpointHit);
    }
}
