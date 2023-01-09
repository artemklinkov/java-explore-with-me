package ru.practicum.explore.model.event;

import lombok.*;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@Entity(name = "events")
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String title;
    private String annotation;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Embedded
    @AttributeOverride(name = "lat", column = @Column(name = "location_lat"))
    @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    private User initiator;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private Set<Compilation> compilations = new HashSet<>();
}
