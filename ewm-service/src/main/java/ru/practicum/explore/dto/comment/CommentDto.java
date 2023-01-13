package ru.practicum.explore.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.model.comment.CommentStatus;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Data
@Builder
public class CommentDto {

    private Long id;
    @Size(min = 5, max = 2000)
    private String text;
    private UserShortDto author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
    private EventShortDto event;
    private CommentStatus status;
}
