package ru.practicum.explore.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentRequest {

    @NotNull
    private Long id;
    @Size(min = 5, max = 2000)
    private String text;
}
