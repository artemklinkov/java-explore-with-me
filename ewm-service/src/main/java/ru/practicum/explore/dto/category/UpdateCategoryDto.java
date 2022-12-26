package ru.practicum.explore.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateCategoryDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
