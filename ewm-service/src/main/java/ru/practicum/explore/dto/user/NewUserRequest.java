package ru.practicum.explore.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUserRequest {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
}
