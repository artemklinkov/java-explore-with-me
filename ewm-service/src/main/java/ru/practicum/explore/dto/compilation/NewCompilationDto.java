package ru.practicum.explore.dto.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class NewCompilationDto {

    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
