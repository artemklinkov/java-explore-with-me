package ru.practicum.explore.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.dto.stats.EndpointHit;
import ru.practicum.explore.dto.stats.ViewStats;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class StatClientImpl implements StatClient {

    private final RestTemplate rest;

    public StatClientImpl(@Value("${stat.service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @Override
    public void createHit(EndpointHit endpointHit) {
        log.info("Sending dto with URI {} to stat server", endpointHit.getUri());
        HttpEntity<EndpointHit> requestEntity = new HttpEntity<>(endpointHit, defaultHeaders());
        rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    @Override
    public List<ViewStats> findStats(String start, String end, List<String> uris, Boolean unique) {
        log.info("Stats start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        StringBuilder builder = new StringBuilder(String.format("/stats?start=%s&end=%s",
                start, end));
        if (uris != null) {
            builder.append(String.format("&uris=%s", String.join(",", uris)));
        }
        if (unique != null) {
            builder.append(String.format("&unique=%s", unique));
        }
        ViewStats[] viewStats = rest.getForObject(builder.toString(), ViewStats[].class);
        return viewStats == null ? Collections.emptyList() : Arrays.asList(viewStats);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}