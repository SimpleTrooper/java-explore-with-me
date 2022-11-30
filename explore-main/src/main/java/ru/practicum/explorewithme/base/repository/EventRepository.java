package ru.practicum.explorewithme.base.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.base.model.Event;

import java.util.Collection;
import java.util.List;

/**
 * Репозиторий событий
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event>,
        EventRepositoryCustom {
    List<Event> findAllByIdIn(Collection<Long> ids);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}
