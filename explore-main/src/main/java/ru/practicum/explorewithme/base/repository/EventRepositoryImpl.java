package ru.practicum.explorewithme.base.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.explorewithme.base.model.ConfirmedRequestsCount;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.model.QEvent;
import ru.practicum.explorewithme.base.model.QRequest;

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
    private final RequestRepository requestRepository;
    private final ExploreClient exploreClient;
    private final JPAQueryFactory jpaQueryFactory;

    public EventRepositoryImpl(@Lazy EventRepository eventRepository,
                               @Lazy RequestRepository requestRepository,
                               @Lazy ExploreClient exploreClient,
                               @Lazy EntityManager entityManager) {
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.exploreClient = exploreClient;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<EventWithReqAndViews> findAllByIdInWithViews(Collection<Long> ids) {
        List<Event> events = eventRepository.findAllByIdIn(ids);
        return makeEventsWithReqAndViews(events);
    }

    @Override
    public List<EventWithReqAndViews> findAllByInitiatorIdWithViews(Long initiatorId, Pageable pageable) {
        List<Event> events = eventRepository.findAllByInitiatorId(initiatorId, pageable);
        return makeEventsWithReqAndViews(events);
    }

    @Override
    public List<EventWithReqAndViews> findAllWithViews(BooleanBuilder booleanBuilder, Pageable pageable) {
        List<Event> events = eventRepository.findAll(booleanBuilder, pageable).toList();
        return makeEventsWithReqAndViews(events);
    }

    @Override
    public Optional<EventWithReqAndViews> findByIdWithViews(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        Long confirmedRequests = getConfirmedParticipantCount(eventId);
        Long views = getViewsForEvent(eventId);
        return Optional.of(new EventWithReqAndViews(event.get(), confirmedRequests, views));
    }

    @Override
    public List<EventWithReqAndViews> findAllAvailableWithViews(BooleanBuilder booleanBuilder,
                                                                Pageable pageable) {
        List<Event> events = jpaQueryFactory.selectFrom(QEvent.event)
                .leftJoin(QRequest.request)
                .on(QEvent.event.id.eq(QRequest.request.event.id))
                .where(booleanBuilder)
                .groupBy(QEvent.event.id)
                .having(QRequest.request.count().isNull()
                        .or(QRequest.request.count().lt(QEvent.event.participantLimit)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return makeEventsWithReqAndViews(events);
    }

    @Override
    public List<EventWithReqAndViews> findAllSortedByViews(BooleanBuilder booleanBuilder,
                                                           Pageable pageable) {
        List<Event> events = StreamSupport.stream(eventRepository.findAll(booleanBuilder).spliterator(), false)
                .collect(Collectors.toList());

        return makeEventsWithReqAndViews(events).stream()
                .sorted(Comparator.comparingLong(EventWithReqAndViews::getViews).reversed())
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public List<EventWithReqAndViews> findAllAvailableSortedByViews(BooleanBuilder booleanBuilder,
                                                                    Pageable pageable) {
        List<Event> events = jpaQueryFactory.selectFrom(QEvent.event)
                .leftJoin(QRequest.request)
                .on(QEvent.event.id.eq(QRequest.request.event.id))
                .where(booleanBuilder)
                .groupBy(QEvent.event.id)
                .having(QRequest.request.count().isNull()
                        .or(QRequest.request.count().lt(QEvent.event.participantLimit)))
                .fetch();

        return makeEventsWithReqAndViews(events).stream()
                .sorted(Comparator.comparingLong(EventWithReqAndViews::getViews).reversed())
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    private List<EventWithReqAndViews> makeEventsWithReqAndViews(List<Event> events) {
        final Map<Long, Long> eventsViews;
        final Map<Long, Long> confirmedRequests;
        if (events.size() != 0) {
            List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
            eventsViews = exploreClient.getViewsForEvents(eventIds);
            confirmedRequests = requestRepository.findAllConfirmedRequests(eventIds).stream()
                    .collect(Collectors.toMap(ConfirmedRequestsCount::getEventId,
                            ConfirmedRequestsCount::getConfirmedCount));
        } else {
            eventsViews = new HashMap<>();
            confirmedRequests = new HashMap<>();
        }
        return events.stream().map(e -> new EventWithReqAndViews(e,
                        confirmedRequests.getOrDefault(e.getId(), 0L),
                        eventsViews.getOrDefault(e.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private Long getViewsForEvent(Long eventId) {
        return exploreClient.getViewsForEvents(List.of(eventId))
                .getOrDefault(eventId, 0L);
    }

    private Long getConfirmedParticipantCount(Long eventId) {
        return requestRepository.findAllConfirmedRequests(eventId).orElse(0L);
    }
}
