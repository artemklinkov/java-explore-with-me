package ru.practicum.explore.service;

import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Component
public class DateTimeDecoder {

    public LocalDateTime dateEncoder(String dateTime) {
        return LocalDateTime.parse(
                URLDecoder.decode(dateTime, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }
}
