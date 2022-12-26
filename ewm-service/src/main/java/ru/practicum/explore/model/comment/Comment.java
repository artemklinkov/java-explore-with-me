package ru.practicum.explore.model.comment;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explore.model.User;
import ru.practicum.explore.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;
    @CreationTimestamp
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
