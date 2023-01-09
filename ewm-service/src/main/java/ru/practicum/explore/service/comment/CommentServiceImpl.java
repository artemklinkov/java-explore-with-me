package ru.practicum.explore.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.comment.mapper.CommentMapper;
import ru.practicum.explore.handler.exception.AccessLevelException;
import ru.practicum.explore.handler.exception.NotAvailableException;
import ru.practicum.explore.handler.exception.NotFoundException;
import ru.practicum.explore.model.User;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentStatus;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> findCommentsByEvent(Long eventId, PageRequest pageRequest) {
        return commentRepository.findAllByEventIdAndStatus(eventId, CommentStatus.CONFIRMED, pageRequest)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto findCommentById(Long commentId) {
        return commentMapper.toDto(validateCommentByStatus(commentId, CommentStatus.CONFIRMED));
    }

    @Override
    public CommentDto createCommentByUser(Long userId, NewCommentDto newCommentDto) {
        Comment comment = commentMapper.toComment(newCommentDto);
        Event event = validateEvent(newCommentDto.getEventId());
        User user = validateUser(userId);
        comment.setEvent(event);
        comment.setAuthor(user);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateCommentByUser(Long userId, Long commentId, CommentDto updateCommentRequest) {
        Comment comment = validateAuthority(userId, commentId);
        if (!comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new NotAvailableException("The comment is already published or rejected");
        }
        comment.setText(updateCommentRequest.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {
        validateAuthority(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findCommentsByUser(Long userId, PageRequest pageRequest) {
        return commentRepository.findAllByAuthorId(userId, pageRequest)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto publishComment(Long commentId) {
        Comment comment = validateCommentByStatus(commentId, CommentStatus.PENDING);
        validateStatus(comment);
        comment.setStatus(CommentStatus.CONFIRMED);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto rejectComment(Long commentId) {
        Comment comment = validateCommentByStatus(commentId, CommentStatus.PENDING);
        validateStatus(comment);
        comment.setStatus(CommentStatus.REJECTED);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> findCommentsByAdmin(List<Long> users, List<CommentStatus> states, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, PageRequest pageRequest) {
        return commentRepository.findCommentsByAdmin(users, states, rangeStart, rangeEnd, pageRequest)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
    }

    private Event validateEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found"));
    }

    private Comment validateCommentByStatus(Long commentId, CommentStatus status) {
        return commentRepository.findCommentByIdAndStatus(commentId, status)
                .orElseThrow(() -> new NotFoundException("Comment is not found"));
    }

    private Comment validateComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment is not found"));
    }

    private Comment validateAuthority(Long userId, Long commentId) {
        Comment comment = validateComment(commentId);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new AccessLevelException("User must be an author of the comment");
        }
        return comment;
    }

    private void validateStatus(Comment comment) {
        if (comment.getStatus() != CommentStatus.PENDING) {
            throw new NotAvailableException("Comment should be in a pending state");
        }
    }
}
