package ru.practicum.explore.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.service.compilation.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Public API to work with events' compilations
 */

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> findCompilations(@RequestParam(defaultValue = "true") Boolean pinned,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size,
                                                 HttpServletRequest request) {
        log.info("{}: {};  find compilations from {} size {}",
                request.getRequestURI(), request.getRemoteAddr(), from, size);
        return compilationService.findCompilations(pinned, PageRequest.of(from / size, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId,
                                              HttpServletRequest request) {
        log.info("{}: {}; find compilation where id = {}",
                request.getRequestURI(), request.getRemoteAddr(), compId);
        return compilationService.findCompilationById(compId);
    }
}
