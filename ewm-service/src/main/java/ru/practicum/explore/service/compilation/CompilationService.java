package ru.practicum.explore.service.compilation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explore.dto.compilation.CompilationDto;

import java.util.List;
import java.util.Set;

public interface CompilationService {

    List<CompilationDto> findCompilations(Boolean pinned, PageRequest pageRequest);

    CompilationDto findCompilationById(Long compId);

    CompilationDto createCompilationOfEvents(String title, Boolean isPinned, Set<Long> events);

    void deleteCompilation(Long compId);

    void addEventToCompilation(Long eventId, Long compId);

    void deleteEventFromCompilation(Long eventId, Long compId);

    void changePinnedStatus(long compilationId, boolean isPinned);
}
