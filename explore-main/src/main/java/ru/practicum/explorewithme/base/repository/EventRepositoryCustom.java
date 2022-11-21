package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryCustom {
    List<EventWithReqAndViews> findAllByIdInWithViews(Collection<Long> ids);

    List<EventWithReqAndViews> findAllByInitiatorIdWithViews(Long initiatorId, Pageable pageable);

    List<EventWithReqAndViews> findAllWithViews(BooleanBuilder booleanBuilder, Pageable pageable);

    List<EventWithReqAndViews> findAllAvailableWithViews(BooleanBuilder booleanBuilder, Pageable pageable);

    List<EventWithReqAndViews> findAllSortedByViews(BooleanBuilder booleanBuilder, Pageable pageable);

    List<EventWithReqAndViews> findAllAvailableSortedByViews(BooleanBuilder booleanBuilder, Pageable pageable);

    Optional<EventWithReqAndViews> findByIdWithViews(Long eventId);
}
