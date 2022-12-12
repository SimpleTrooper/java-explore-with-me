package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import ru.practicum.exploreclient.ExploreClientForStats;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventWithViews;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Кастомный репозиторий для получения числа подтвержденных участий и просмотров события
 */
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final EventRepository eventRepository;
    private final ExploreClientForStats exploreClientForStats;
    private final JPAQueryFactory jpaQueryFactory;

    public EventRepositoryImpl(@Lazy EventRepository eventRepository,
                               @Lazy ExploreClientForStats exploreClientForStats,
                               @Lazy EntityManager entityManager) {
        this.eventRepository = eventRepository;
        this.exploreClientForStats = exploreClientForStats;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<EventWithViews> findAllByIdInWithViews(Collection<Long> ids) {
        List<Event> events = eventRepository.findAllByIdIn(ids);
        return makeEventsWithViews(events);
    }

    @Override
    public List<EventWithViews> findAllByInitiatorIdWithViews(Long initiatorId, Pageable pageable) {
        List<Event> events = eventRepository.findAllByInitiatorId(initiatorId, pageable);
        return makeEventsWithViews(events);
    }

    @Override
    public List<EventWithViews> findAllWithViews(BooleanBuilder booleanBuilder, Pageable pageable) {
        List<Event> events = eventRepository.findAll(booleanBuilder, pageable).toList();
        return makeEventsWithViews(events);
    }

    @Override
    public Optional<EventWithViews> findByIdWithViews(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        Long views = getViewsForEvent(eventId);
        return Optional.of(new EventWithViews(event.get(), views));
    }

    @Override
    public List<EventWithViews> findAllSortedByViews(BooleanBuilder booleanBuilder,
                                                     Pageable pageable) {
        List<Event> events = StreamSupport.stream(eventRepository.findAll(booleanBuilder).spliterator(), false)
                .collect(Collectors.toList());

        return makeEventsWithViews(events).stream()
                .sorted(Comparator.comparingLong(EventWithViews::getViews).reversed())
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    private List<EventWithViews> makeEventsWithViews(List<Event> events) {
        final Map<Long, Long> eventsViews;
        if (events.size() != 0) {
            List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
            eventsViews = exploreClientForStats.getViewsForEvents(eventIds);
        } else {
            eventsViews = new HashMap<>();
        }
        return events.stream().map(e -> new EventWithViews(e, eventsViews.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private Long getViewsForEvent(Long eventId) {
        return exploreClientForStats.getViewsForEvents(List.of(eventId))
                .getOrDefault(eventId, 0L);
    }
}
