package ru.practicum.explorewithme.apiprivate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apiprivate.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateRequestService;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.exception.RequestNotFoundException;
import ru.practicum.explorewithme.base.exception.UserNotFoundException;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.RequestRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики запросов на участие
 */
@Service
@Transactional(readOnly = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public PrivateRequestServiceImpl(UserRepository userRepository,
                                     EventRepository eventRepository,
                                     RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<ParticipationRequestDto> find(Long userId, Long eventId) {
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        checkEventOwnership(user, event);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(ParticipationRequestDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirm(Long userId, Long eventId, Long requestId) {
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        checkEventOwnership(user, event);
        Request request = findByIdOrThrow(requestId);
        Long confirmedParticipantCount = requestRepository.findAllConfirmedRequests(eventId).orElse(0L);
        if (confirmedParticipantCount >= event.getParticipantLimit()) {
            throw new ConditionsNotMetException("Participant limit reached");
        }
        request.setRequestState(RequestState.CONFIRMED);
        if (confirmedParticipantCount == event.getParticipantLimit() - 1) {
            List<Request> pendingRequests = requestRepository.findAllPendingRequestsForEvent(eventId);
            for (Request pendingRequest: pendingRequests) {
                pendingRequest.setRequestState(RequestState.REJECTED);
            }
        }
        return ParticipationRequestDto.from(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto reject(Long userId, Long eventId, Long requestId) {
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        checkEventOwnership(user, event);
        Request request = findByIdOrThrow(requestId);
        request.setRequestState(RequestState.REJECTED);
        return ParticipationRequestDto.from(request);
    }

    @Override
    public List<ParticipationRequestDto> findByUserId(Long userId) {
        findUserByIdOrThrow(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto add(Long userId, Long eventId) {
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConditionsNotMetException("Can't add same request");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConditionsNotMetException("Initiator can't add request for participation");
        }
        if (!event.getEventState().equals(EventState.PUBLISHED)) {
            throw new ConditionsNotMetException("Can participate only in PUBLISHED events");
        }
        Long confirmedRequests = requestRepository.findAllConfirmedRequests(eventId).orElse(0L);
        if (confirmedRequests >= event.getParticipantLimit()) {
            throw new ConditionsNotMetException("Participant limit reached");
        }
        RequestState requestState = RequestState.PENDING;
        if (!event.getRequestModeration()) {
            requestState = RequestState.CONFIRMED;
        }
        Request request = new Request(null, user, event, requestState,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        requestRepository.save(request);
        return ParticipationRequestDto.from(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        findUserByIdOrThrow(userId);
        Request request = findByIdOrThrow(requestId);
        if (!userId.equals(request.getRequester().getId())) {
            throw new ConditionsNotMetException(String.format("User with id = %d is not " +
                    "requester for request with id = %d", userId, requestId));
        }
        request.setRequestState(RequestState.CANCELED);
        return ParticipationRequestDto.from(request);
    }

    private void checkEventOwnership(User user, Event event) {
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConditionsNotMetException(String.format("User with id = %d is not initiator " +
                    "of the event with id = %d", user.getId(), event.getId()));
        }
    }

    private Request findByIdOrThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(String.format("Request with id = %d is not found",
                        requestId)));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %d is not found",
                        userId)));
    }

    private Event findEventByIdOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d is not found",
                        eventId)));
    }
}
