package ru.practicum.explorewithme.admin.service.impl;

import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.admin.controller.request.AdminGetEventsRequest;
import ru.practicum.explorewithme.admin.dto.AdminEventFullDto;
import ru.practicum.explorewithme.admin.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.admin.service.AdminEventService;
import ru.practicum.explorewithme.base.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.model.QEvent;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики событий - admin API
 */
@Service
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminEventServiceImpl(EventRepository eventRepository,
                                 CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<AdminEventFullDto> findAll(AdminGetEventsRequest getEventsRequest, PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.makeOffsetBasedByFieldAsc("id");
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (getEventsRequest.getUsers() != null && getEventsRequest.getUsers().size() != 0) {
            booleanBuilder.and(QEvent.event.initiator.id.in(getEventsRequest.getUsers()));
        }
        if (getEventsRequest.getStates() != null && getEventsRequest.getStates().size() != 0) {
            booleanBuilder.and(QEvent.event.eventState.in(getEventsRequest.getStates()));
        }
        if (getEventsRequest.getCategories() != null && getEventsRequest.getCategories().size() != 0) {
            booleanBuilder.and(QEvent.event.category.id.in(getEventsRequest.getCategories()));
        }
        if (getEventsRequest.getRangeStart() != null) {
            booleanBuilder.and(QEvent.event.eventDate.goe(getEventsRequest.getRangeStart()));
        }
        if (getEventsRequest.getRangeEnd() != null) {
            booleanBuilder.and(QEvent.event.eventDate.loe(getEventsRequest.getRangeEnd()));
        }
        List<EventWithReqAndViews> events = eventRepository.findAllWithViews(booleanBuilder, pageable);

        return events.stream().map(e -> AdminEventFullDto.from(e.getEvent(), e.getConfirmedRequests(), e.getViews()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminEventFullDto update(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        EventWithReqAndViews updatedEvent = findByIdWithViewsOrThrow(eventId);
        updateRequiredFields(updatedEvent.getEvent(), adminUpdateEventRequest);

        return AdminEventFullDto.from(updatedEvent.getEvent(), updatedEvent.getConfirmedRequests(),
                updatedEvent.getViews());
    }

    private void updateRequiredFields(Event event, AdminUpdateEventRequest adminUpdateEventRequest) {
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            event.setCategory(findCategoryByIdOrThrow(adminUpdateEventRequest.getCategory()));
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
    }

    @Override
    @Transactional
    public AdminEventFullDto publish(Long eventId) {
        EventWithReqAndViews eventWithViews = findByIdWithViewsOrThrow(eventId);
        Event event = eventWithViews.getEvent();

        LocalDateTime publishedOn = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (publishedOn.plusHours(1L).isAfter(event.getEventDate())) {
            throw new ConditionsNotMetException(String.format("Event must be published at least one " +
                    "hour before starting time, eventDate = %s, eventId = %d", event.getEventDate(), eventId));
        }

        if (!event.getEventState().equals(EventState.PENDING)) {
            throw new ConditionsNotMetException(String.format("Event state must be PENDING, " +
                    "eventState = %s, eventId = %d", event.getEventState(), eventId));
        }

        event.setPublishedOn(publishedOn);
        event.setEventState(EventState.PUBLISHED);

        return AdminEventFullDto.from(event, eventWithViews.getConfirmedRequests(), eventWithViews.getViews());
    }

    @Override
    @Transactional
    public AdminEventFullDto reject(Long eventId) {
        EventWithReqAndViews eventWithViews = findByIdWithViewsOrThrow(eventId);
        Event event = eventWithViews.getEvent();

        if (event.getEventState().equals(EventState.PUBLISHED)) {
            throw new ConditionsNotMetException(String.format("Event state must not be PUBLISHED, " +
                    "eventState = %s, eventId = %d", event.getEventState(), eventId));
        }

        event.setEventState(EventState.CANCELED);

        return AdminEventFullDto.from(event, eventWithViews.getConfirmedRequests(), eventWithViews.getViews());
    }

    private EventWithReqAndViews findByIdWithViewsOrThrow(Long eventId) {
        return eventRepository.findByIdWithViews(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d " +
                        "is not found", eventId)));
    }

    private Category findCategoryByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id = %d " +
                        "is not found", categoryId)));
    }
}
