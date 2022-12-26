package ru.practicum.explore.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.service.compilation.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * API for admin to work with events' compilations
 */

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto compilationDto,
                                            HttpServletRequest request) {
        log.info("{}: {}; create compilation {}", request.getRequestURI(), request.getRemoteAddr(),
                compilationDto.toString());
        return compilationService.createCompilationOfEvents(
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                compilationDto.getEvents()
        );
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId,
                                  HttpServletRequest request) {
        log.info("{}: {}; delete compilation with id = {}", request.getRequestURI(), request.getRemoteAddr(), compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId,
                                           HttpServletRequest request) {
        log.info("{}: {}; delete an event with id = {} from compilation with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), eventId, compId);
        compilationService.deleteEventFromCompilation(eventId, compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId,
                                      HttpServletRequest request) {
        log.info("{}: {}; add an event with id = {} to compilation with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), eventId, compId);
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId,
                                 HttpServletRequest request) {
        log.info("{}: {}; unpin compilation with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), compId);
        compilationService.changePinnedStatus(compId, false);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId,
                               HttpServletRequest request) {
        log.info("{}: {}; pin compilation with id = {}",
                request.getRequestURI(), request.getRemoteAddr(), compId);
        compilationService.changePinnedStatus(compId, true);
    }
}
