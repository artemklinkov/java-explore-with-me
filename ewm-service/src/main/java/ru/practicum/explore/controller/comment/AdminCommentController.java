package ru.practicum.explore.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.model.comment.CommentStatus;
import ru.practicum.explore.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.utils.Constant.DEFAULT_DATE_TIME_FORMAT;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; publish comment with id = {}", request.getRequestURI(), request.getRemoteAddr(), commentId);
        return commentService.publishComment(commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto rejectComment(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; reject comment with id = {}", request.getRequestURI(), request.getRemoteAddr(), commentId);
        return commentService.rejectComment(commentId);
    }

    @GetMapping
    public List<CommentDto> adminFindComments(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<CommentStatus> states,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("{}: {}; admin gets a list of comments", request.getRequestURI(), request.getRemoteAddr());
        return commentService.findCommentsByAdmin(users, states, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByUser(@PathVariable Long commentId,
                                    HttpServletRequest request) {
        log.info("{}: {}; delete comment with id = {} by admin", request.getRequestURI(),
                request.getRemoteAddr(), commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
