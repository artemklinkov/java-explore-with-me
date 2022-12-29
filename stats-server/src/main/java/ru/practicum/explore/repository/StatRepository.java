package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.explore.dto.ViewStats(app, uri, COUNT(DISTINCT ip)) " +
            "FROM hits " +
            "WHERE uri IN :uris AND (timestamp BETWEEN :start AND :end) " +
            "GROUP BY app, uri"
    )
    List<ViewStats> calculateUniqueStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.dto.ViewStats(app, uri, COUNT(ip)) " +
            "FROM hits " +
            "WHERE uri IN :uris AND (timestamp BETWEEN :start AND :end) " +
            "GROUP BY app, uri"
    )
    List<ViewStats> calculateStats(List<String> uris, LocalDateTime start, LocalDateTime end);
}
