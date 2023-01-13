package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.request.Request;
import ru.practicum.explore.model.request.RequestState;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Integer countByEventIdAndState(Long id, RequestState state);

    List<Request> findByRequesterId(Long id);

    List<Request> findByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long userId);

    @Modifying
    @Query("UPDATE requests AS r " +
            "SET r.state = 'REJECTED' " +
            "WHERE r.state = 'PENDING' " +
            "AND r.event = :eventId")
    void rejectPendingRequests(Long eventId);
}
