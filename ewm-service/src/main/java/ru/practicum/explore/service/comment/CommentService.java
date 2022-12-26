package ru.practicum.explore.service.comment;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.comment.UpdateCommentRequest;
import ru.practicum.explore.model.comment.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    List<CommentDto> findCommentsByEvent(Long eventId, PageRequest pageRequest);

    CommentDto findCommentById(Long commentId);

    CommentDto createCommentByUser(Long userId, NewCommentDto newCommentDto);

    CommentDto updateCommentByUser(Long userId, UpdateCommentRequest updateCommentRequest);

    void deleteCommentByUser(Long userId, Long commentId);

    List<CommentDto> findCommentsByUser(Long userId, PageRequest pageRequest);

    CommentDto publishComment(Long commentId);

    CommentDto rejectComment(Long commentId);

    List<CommentDto> findCommentsByAdmin(List<Long> users, List<CommentStatus> states, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, PageRequest pageRequest);
}
