package ru.practicum.explore.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.model.request.RequestState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@Data
@Builder
public class ParticipationRequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime created;
    @NotNull
    private Long event;
    private Long id;
    @NotNull
    private Long requester;
    private RequestState status;
}
