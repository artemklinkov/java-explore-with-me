package ru.practicum.explore.dto.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.comment.CommentDto;
import ru.practicum.explore.dto.comment.NewCommentDto;
import ru.practicum.explore.dto.event.mapper.EventMapper;
import ru.practicum.explore.dto.user.mapper.UserMapper;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentStatus;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(userMapper.toShortDto(comment.getAuthor()))
                .createdAt(comment.getCreated())
                .event(eventMapper.toShortDto(comment.getEvent()))
                .status(comment.getStatus())
                .build();
    }

    public Comment toComment(NewCommentDto dto) {
        return Comment.builder()
                .text(dto.getText())
                .status(CommentStatus.PENDING)
                .build();
    }
}
