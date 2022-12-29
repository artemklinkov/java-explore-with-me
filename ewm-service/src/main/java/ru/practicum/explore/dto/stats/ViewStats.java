package ru.practicum.explore.dto.stats;

import lombok.Data;

@Data
public class ViewStats {

    private String app;
    private String uri;
    private Long hits;
}
