package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.explore.model.event.Location;

import java.time.LocalDateTime;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Data
public class AdminUpdateEventRequest {

    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
