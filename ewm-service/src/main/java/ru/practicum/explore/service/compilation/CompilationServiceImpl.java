package ru.practicum.explore.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.mapper.CompilationMapper;
import ru.practicum.explore.handler.exception.NotFoundException;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public List<CompilationDto> findCompilations(Boolean pinned, PageRequest pageRequest) {
        if (pinned != null) {
            return compilationRepository.findByPinned(pinned, pageRequest)
                    .stream()
                    .map(compilationMapper::toDto)
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(compilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilationById(Long compId) {
        return compilationMapper.toDto(validateCompilation(compId));
    }

    @Override
    @Transactional
    public CompilationDto createCompilationOfEvents(String title, Boolean isPinned, Set<Long> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(title);
        compilation.setPinned(isPinned);
        List<Event> eventsList = eventRepository.findByIdIn(events);
        compilation.setEvents(new HashSet<>(eventsList));
        eventsList.forEach(event -> event.getCompilations().add(compilation));

        compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long eventId, Long compId) {
        Compilation compilation = validateCompilation(compId);
        Event event = validateEvent(eventId);

        compilation.getEvents().add(event);
        event.getCompilations().add(compilation);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long eventId, Long compId) {
        Compilation compilation = validateCompilation(compId);
        Event event = validateEvent(eventId);

        compilation.getEvents().remove(event);
        event.getCompilations().remove(compilation);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void changePinnedStatus(long compId, boolean isPinned) {
        Compilation compilation = validateCompilation(compId);
        compilation.setPinned(isPinned);
        compilationRepository.save(compilation);
    }

    private Compilation validateCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found"));
    }

    private Event validateEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found"));
    }
}
