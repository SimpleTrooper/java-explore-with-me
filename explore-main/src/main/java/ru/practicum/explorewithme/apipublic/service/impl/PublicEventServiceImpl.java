package ru.practicum.explorewithme.apipublic.service.impl;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.apipublic.service.PublicEventService;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.model.QEvent;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики событий
 */
@Service
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;

    @Autowired
    public PublicEventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<PublicEventShortDto> findAll(PublicGetEventsRequest publicGetEventsRequest,
                                             PaginationRequest paginationRequest) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(QEvent.event.eventState.eq(EventState.PUBLISHED));
        if (publicGetEventsRequest.getText() != null) {
            String text = publicGetEventsRequest.getText();
            booleanBuilder.and(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        }

        List<Long> categories = publicGetEventsRequest.getCategories();
        if (categories != null && categories.size() != 0) {
            booleanBuilder.and(QEvent.event.category.id.in(categories));
        }

        if (publicGetEventsRequest.getPaid() != null) {
            booleanBuilder.and(QEvent.event.paid.eq(publicGetEventsRequest.getPaid()));
        }

        LocalDateTime startDate = publicGetEventsRequest.getRangeStart();
        if (startDate == null) {
            startDate = LocalDateTime.now();
        }

        booleanBuilder.and(QEvent.event.eventDate.goe(startDate));

        if (publicGetEventsRequest.getRangeEnd() != null) {
            booleanBuilder.and(QEvent.event.eventDate.loe(publicGetEventsRequest.getRangeEnd()));
        }

        List<EventWithReqAndViews> eventsWithReqAndViews = new ArrayList<>();

        if (publicGetEventsRequest.getOnlyAvailable()) {
            if (publicGetEventsRequest.getSort() != null) {
                switch (publicGetEventsRequest.getSort()) {
                    case EVENT_DATE:
                        eventsWithReqAndViews = eventRepository.findAllAvailableWithViews(booleanBuilder,
                                paginationRequest.makeOffsetBasedByFieldDesc("eventDate"));
                        break;
                    case VIEWS:
                        eventsWithReqAndViews = eventRepository.findAllAvailableSortedByViews(booleanBuilder,
                                paginationRequest.makeOffsetBased());
                }
            } else {
                eventsWithReqAndViews = eventRepository.findAllAvailableWithViews(booleanBuilder,
                        paginationRequest.makeOffsetBased());
            }
        } else {
            if (publicGetEventsRequest.getSort() != null) {
                switch (publicGetEventsRequest.getSort()) {
                    case EVENT_DATE:
                        eventsWithReqAndViews = eventRepository.findAllWithViews(booleanBuilder,
                                paginationRequest.makeOffsetBasedByFieldDesc("eventDate"));
                        break;
                    case VIEWS:
                        eventsWithReqAndViews = eventRepository.findAllSortedByViews(booleanBuilder,
                                paginationRequest.makeOffsetBased());
                }
            } else {
                eventsWithReqAndViews = eventRepository.findAllWithViews(booleanBuilder,
                        paginationRequest.makeOffsetBased());
            }
        }

        return eventsWithReqAndViews.stream()
                .map(event -> PublicEventShortDto.from(event.getEvent(),
                        event.getConfirmedRequests(), event.getViews()))
                .collect(Collectors.toList());
    }

    @Override
    public PublicEventFullDto findById(Long eventId) {
        EventWithReqAndViews event = findByIdOrThrow(eventId);
        if (!event.getEvent().getEventState().equals(EventState.PUBLISHED)) {
            throw new ConditionsNotMetException("Event state is not PUBLISHED");
        }
        return PublicEventFullDto.from(event.getEvent(), event.getConfirmedRequests(), event.getViews());
    }

    private EventWithReqAndViews findByIdOrThrow(Long eventId) {
        return eventRepository.findByIdWithViews(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d " +
                        "is not found", eventId)));
    }
}
