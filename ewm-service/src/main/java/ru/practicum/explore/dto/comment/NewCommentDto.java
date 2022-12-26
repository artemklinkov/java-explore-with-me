package ru.practicum.explore.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {

    @NotNull
    private Long eventId;
    @Size(min = 5, max = 2000)
    private String text;
}
