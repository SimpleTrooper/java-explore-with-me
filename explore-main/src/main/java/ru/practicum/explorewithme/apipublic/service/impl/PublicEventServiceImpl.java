package ru.practicum.explorewithme.apipublic.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.apipublic.service.PublicEventService;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.exception.LocationNotFoundException;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.EventWithViews;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.QEvent;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.LocationRepository;

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
    private final LocationRepository locationRepository;

    @Autowired
    public PublicEventServiceImpl(EventRepository eventRepository,
                                  LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
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

        if (publicGetEventsRequest.getLocation() != null) {
            Location location = findLocationByIdOrThrow(publicGetEventsRequest.getLocation());
            booleanBuilder.and(Expressions.numberTemplate(Double.class,"distance({0}, {1}, {2}, {3})",
                    location.getLat(), location.getLon(),
                    QEvent.event.locationCoordinates.lat, QEvent.event.locationCoordinates.lon)
                    .loe(location.getRadius()));
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

        if (publicGetEventsRequest.getOnlyAvailable()) {
            booleanBuilder.and(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit));
        }

        List<EventWithViews> eventsWithViews = new ArrayList<>();
        if (publicGetEventsRequest.getSort() != null) {
            switch (publicGetEventsRequest.getSort()) {
                case EVENT_DATE:
                    eventsWithViews = eventRepository.findAllWithViews(booleanBuilder,
                            paginationRequest.makeOffsetBasedByFieldDesc("eventDate"));
                    break;
                case VIEWS:
                    eventsWithViews = eventRepository.findAllSortedByViews(booleanBuilder,
                            paginationRequest.makeOffsetBased());
            }
        } else {
            eventsWithViews = eventRepository.findAllWithViews(booleanBuilder,
                    paginationRequest.makeOffsetBased());
        }

        return eventsWithViews.stream()
                .map(event -> PublicEventShortDto.from(event.getEvent(), event.getViews()))
                .collect(Collectors.toList());
    }

    @Override
    public PublicEventFullDto findById(Long eventId) {
        EventWithViews event = findByIdOrThrow(eventId);
        if (!event.getEvent().getEventState().equals(EventState.PUBLISHED)) {
            throw new ConditionsNotMetException("Event state is not PUBLISHED");
        }
        return PublicEventFullDto.from(event.getEvent(), event.getViews());
    }

    private EventWithViews findByIdOrThrow(Long eventId) {
        return eventRepository.findByIdWithViews(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d " +
                        "is not found", eventId)));
    }

    private Location findLocationByIdOrThrow(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(String.format("Location with id = %d " +
                        "is not found", locationId)));
    }
}
