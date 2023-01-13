package ru.practicum.explore.dto.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.event.mapper.EventMapper;
import ru.practicum.explore.model.Compilation;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream().map(eventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
