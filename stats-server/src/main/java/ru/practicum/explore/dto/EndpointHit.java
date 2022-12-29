package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class EndpointHit {

    Long id;
    String app;
    String uri;
    String ip;
    String timestamp;
}
