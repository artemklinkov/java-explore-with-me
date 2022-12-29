package ru.practicum.explore.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> findCommentsByEvent(@PathVariable Long eventId,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("{}: {}; find comments to event with id = {}", request.getRequestURI(),
                request.getRemoteAddr(), eventId);
        return commentService.findCommentsByEvent(eventId, PageRequest.of(from / size, size));
    }

    @GetMapping("/{commentId}")
    public CommentDto findCommentById(@PathVariable Long eventId,
                                            @PathVariable Long commentId,
                                            HttpServletRequest request) {
        log.info("{}: {}; find comment with id {} to event with id = {}", request.getRequestURI(),
                request.getRemoteAddr(), commentId, eventId);
        return commentService.findCommentById(commentId);
    }
}
