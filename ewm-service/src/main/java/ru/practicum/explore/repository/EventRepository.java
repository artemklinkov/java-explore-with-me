package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM events e WHERE " +
            "(:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM events AS e " +
            "WHERE (:text IS NULL " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findByIdIn(Set<Long> ids);

    List<Event> findEventsByCategoryId(Long categoryId);

}
