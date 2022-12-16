package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.practicum.explorewithme.base.model.EventWithViews;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryCustom {
    @EntityGraph(value = "event-entity-graph")
    List<EventWithViews> findAllByIdInWithViews(Collection<Long> ids);

    @EntityGraph(value = "event-entity-graph")
    List<EventWithViews> findAllByInitiatorIdWithViews(Long initiatorId, Pageable pageable);

    @EntityGraph(value = "event-entity-graph")
    List<EventWithViews> findAllWithViews(BooleanBuilder booleanBuilder, Pageable pageable);

    @EntityGraph(value = "event-entity-graph")
    List<EventWithViews> findAllSortedByViews(BooleanBuilder booleanBuilder, Pageable pageable);

    @EntityGraph(value = "event-entity-graph")
    Optional<EventWithViews> findByIdWithViews(Long eventId);
}
