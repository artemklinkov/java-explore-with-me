package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.service.StatService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatService statService;

    @PostMapping("/hit")
    public EndpointHit createHit(@RequestBody EndpointHit endpointHit, HttpServletRequest request) {
        log.info("{}: {}; add statistics {}",
                request.getRequestURI(),
                request.getRemoteAddr(),
                endpointHit.toString());
        return statService.createHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> findStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique,
                                     HttpServletRequest request) {
        log.info("{}: {}; find statistics", request.getRequestURI(), request.getRemoteAddr());
        return statService.findStats(start, end, uris, unique);
    }
}
