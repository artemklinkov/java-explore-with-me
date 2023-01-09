package ru.practicum.explore.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.service.comment.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto createCommentByUser(@PathVariable Long userId,
                                          @RequestBody @Valid NewCommentDto newCommentDto,
                                          HttpServletRequest request) {
        log.info("{}: {}; create comment {} by user with id = {}", request.getRequestURI(), request.getRemoteAddr(),
                newCommentDto.toString(), userId);
        return commentService.createCommentByUser(userId, newCommentDto);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateCommentByUser(@PathVariable Long userId, @PathVariable Long commentId,
                                          @RequestBody @Valid CommentDto updateCommentRequest,
                                          HttpServletRequest request) {
        log.info("{}: {}; update comment by {} by user with id = {}", request.getRequestURI(), request.getRemoteAddr(),
                updateCommentRequest.toString(), userId);
        return commentService.updateCommentByUser(userId, commentId, updateCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByUser(@PathVariable Long userId, @PathVariable Long commentId,
                                    HttpServletRequest request) {
        log.info("{}: {}; delete comment with id = {} by user with id = {}", request.getRequestURI(),
                request.getRemoteAddr(), commentId, userId);
        commentService.deleteCommentByUser(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> findCommentsByUser(@PathVariable Long userId,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("{}: {}; get list of comments by user with id = {}", request.getRequestURI(),
                request.getRemoteAddr(), userId);
        return commentService.findCommentsByUser(userId, PageRequest.of(from / size, size));
    }
}
