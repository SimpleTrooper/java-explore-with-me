package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.base.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий событий
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event>,
        EventRepositoryCustom {
    @EntityGraph(value = "event-entity-graph")
    Optional<Event> findById(Long id);

    @EntityGraph(value = "event-entity-graph")
    Page<Event> findAll(Pageable pageable);

    @EntityGraph(value = "event-entity-graph")
    Page<Event> findAll(Predicate predicate, Pageable pageable);

    @EntityGraph(value = "event-entity-graph")
    List<Event> findAll(Predicate predicate);

    @EntityGraph(value = "event-entity-graph")
    List<Event> findAllByIdIn(Collection<Long> ids);

    @EntityGraph(value = "event-entity-graph")
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}
