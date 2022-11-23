package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.base.model.EventWithViews;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryCustom {
    List<EventWithViews> findAllByIdInWithViews(Collection<Long> ids);

    List<EventWithViews> findAllByInitiatorIdWithViews(Long initiatorId, Pageable pageable);

    List<EventWithViews> findAllWithViews(BooleanBuilder booleanBuilder, Pageable pageable);

    List<EventWithViews> findAllSortedByViews(BooleanBuilder booleanBuilder, Pageable pageable);

    Optional<EventWithViews> findByIdWithViews(Long eventId);
}
