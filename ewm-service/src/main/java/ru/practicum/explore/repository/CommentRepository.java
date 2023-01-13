package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventIdAndStatus(Long eventId, CommentStatus status, Pageable pageable);

    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);

    Optional<Comment> findCommentByIdAndStatus(Long id, CommentStatus status);

    @Query("SELECT c FROM comments c WHERE " +
            "(:users IS NULL OR c.author.id IN :users) " +
            "AND (:states IS NULL OR c.status IN :states) " +
            "AND (CAST(:rangeEnd AS date) IS NULL " +
            "OR c.created BETWEEN :rangeStart AND :rangeEnd)" +
            "ORDER BY c.id ASC")
    List<Comment> findCommentsByAdmin(List<Long> users, List<CommentStatus> states, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Pageable pageable);
}
