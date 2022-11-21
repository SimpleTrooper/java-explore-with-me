package ru.practicum.explorewithme.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.base.model.ConfirmedRequestsCount;
import ru.practicum.explorewithme.base.model.Request;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий запросов на участие в событии
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT new ru.practicum.explorewithme.base.model.ConfirmedRequestsCount(r.event.id , COUNT(r.id)) " +
            "FROM Request AS r " +
            "WHERE r.requestState = 'CONFIRMED' AND r.event.id IN ?1 " +
            "GROUP BY r.event.id")
    List<ConfirmedRequestsCount> findAllConfirmedRequests(List<Long> eventIds);

    @Query("SELECT COUNT(r.id) " +
            "FROM Request AS r " +
            "WHERE r.requestState = 'CONFIRMED' AND r.event.id = ?1 " +
            "GROUP BY r.event.id")
    Optional<Long> findAllConfirmedRequests(Long eventId);

    @Query("SELECT new Request(r.id, r.requester, r.event, r.requestState, r.createdOn) " +
            "FROM Request AS r " +
            "WHERE r.requestState = 'PENDING' AND r.event.id = ?1")
    List<Request> findAllPendingRequestsForEvent(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);
}
