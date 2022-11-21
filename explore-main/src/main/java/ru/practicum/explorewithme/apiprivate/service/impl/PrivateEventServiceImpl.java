package ru.practicum.explorewithme.apiprivate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apiprivate.dto.NewEventDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventFullDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventShortDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateEventService;
import ru.practicum.explorewithme.base.exception.CategoryNotFoundException;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.exception.UserNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики событий - private API
 */
@Service
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PrivateEventServiceImpl(UserRepository userRepository,
                                   EventRepository eventRepository,
                                   CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<PrivateEventShortDto> findAllByInitiatorId(Long initiatorId, PaginationRequest paginationRequest) {
        findUserByIdOrThrow(initiatorId);
        List<EventWithReqAndViews> events = eventRepository.findAllByInitiatorIdWithViews(initiatorId,
                paginationRequest.makeOffsetBasedByFieldAsc("id"));
        return events.stream().map(event -> PrivateEventShortDto.from(event.getEvent(),
                        event.getConfirmedRequests(), event.getViews()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrivateEventFullDto update(Long userId, NewEventDto newEventDto) {
        Long eventId = newEventDto.getEventId();
        EventWithReqAndViews eventWithViews = findByIdWithViewsOrThrow(eventId);
        Event event = eventWithViews.getEvent();
        findUserByIdOrThrow(userId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConditionsNotMetException(String.format("User with id = %d is not initiator", userId));
        }
        if (event.getEventState() != EventState.CANCELED && event.getEventState() != EventState.PENDING) {
            throw new ConditionsNotMetException(String.format("Event must be in CANCELED or PENDING state, " +
                    "eventState = %s", event.getEventState()));
        }
        updateRequiredFields(event, newEventDto);
        return PrivateEventFullDto.from(event, eventWithViews.getConfirmedRequests(), eventWithViews.getViews());
    }

    private void updateRequiredFields(Event event, NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getCategory() != null) {
            event.setCategory(findCategoryByIdOrThrow(newEventDto.getCategory()));
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(newEventDto.getEventDate());
        }
        if (newEventDto.getLocation() != null) {
            event.setLocation(newEventDto.getLocation());
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
    }

    @Override
    @Transactional
    public PrivateEventFullDto add(Long userId, NewEventDto newEventDto) {
        User initiator = findUserByIdOrThrow(userId);
        Category category = findCategoryByIdOrThrow(newEventDto.getCategory());
        Event newEvent = NewEventDto.toEvent(newEventDto, category, initiator);
        newEvent.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newEvent.setEventState(EventState.PENDING);
        eventRepository.save(newEvent);
        return PrivateEventFullDto.from(newEvent, 0L, 0L);
    }

    @Override
    public PrivateEventFullDto findById(Long userId, Long eventId) {
        findUserByIdOrThrow(userId);
        EventWithReqAndViews eventWithViews = findByIdWithViewsOrThrow(eventId);
        if (!eventWithViews.getEvent().getInitiator().getId().equals(userId)) {
            throw new ConditionsNotMetException(String.format("User with id = %d is not initiator", userId));
        }
        return PrivateEventFullDto.from(eventWithViews.getEvent(), eventWithViews.getConfirmedRequests(),
                eventWithViews.getViews());
    }

    @Override
    @Transactional
    public PrivateEventFullDto cancel(Long userId, Long eventId) {
        findUserByIdOrThrow(userId);
        EventWithReqAndViews eventWithViews = findByIdWithViewsOrThrow(eventId);
        Event event = eventWithViews.getEvent();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConditionsNotMetException(String.format("User with id = %d is not initiator", userId));
        }
        if (event.getEventState() != EventState.PENDING) {
            throw new ConditionsNotMetException(String.format("Event must be in PENDING state, " +
                    "eventState = %s", event.getEventState()));
        }
        event.setEventState(EventState.CANCELED);
        return PrivateEventFullDto.from(event, eventWithViews.getConfirmedRequests(),
                eventWithViews.getViews());
    }

    private EventWithReqAndViews findByIdWithViewsOrThrow(Long eventId) {
        return eventRepository.findByIdWithViews(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d " +
                        "is not found", eventId)));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d " +
                        "is not found", userId)));
    }

    private Category findCategoryByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id = %d " +
                        "is not found", categoryId)));
    }
}
