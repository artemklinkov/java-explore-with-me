package ru.practicum.explore.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.model.comment.CommentStatus;

import java.time.LocalDateTime;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Data
@Builder
public class CommentDto {

    private Long id;
    private String text;
    private UserShortDto author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
    private EventShortDto event;
    private CommentStatus status;
}
