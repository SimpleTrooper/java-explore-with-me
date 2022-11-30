package ru.practicum.explorewithme.apiprivate.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.explorewithme.apiprivate.dto.NewEventDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventFullDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventShortDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateEventService;
import ru.practicum.explorewithme.base.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Интеграционные тесты PrivateEventServiceImpl
 */
@DataJpaTest
@Import({PrivateEventServiceImpl.class, ExploreDateFormatter.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PrivateEventServiceImplTest {
    @MockBean
    ExploreClient exploreClient;
    final UserRepository userRepository;
    @InjectMocks
    final EventRepository eventRepository;
    final CategoryRepository categoryRepository;
    final PrivateEventService privateEventService;

    User user1;
    User user2;
    Category category;
    Event event1;
    Event event2;

    @BeforeEach
    void init() {
        user1 = new User(null, "User 1", "mail1@mail.com");
        user2 = new User(null, "User 2", "mail2@mail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        category = new Category(null, "Category 1");
        categoryRepository.save(category);

        event1 = Event.builder()
                .title("Test event 1")
                .annotation("Test event 1 annotation")
                .description("Test event 1 description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.CANCELED)
                .confirmedRequests(0L)
                .build();
        event2 = Event.builder()
                .title("Test event 2")
                .annotation("Test event 2 annotation")
                .description("Test event 2 description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.CANCELED)
                .confirmedRequests(0L)
                .build();
        eventRepository.save(event1);
        eventRepository.save(event2);
    }

    /**
     * Стандартное поведение shouldFindAllByInitiatorId
     */
    @Test
    void shouldFindAllByInitiatorId() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId());
        Long event1Views = 5L;
        Long event2Views = 10L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views));

        List<PrivateEventShortDto> actual = privateEventService.findAllByInitiatorId(user1.getId(),
                new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(eventIds.size()));
        assertThat(actual.get(0).getId(), equalTo(event1.getId()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(event1.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(event1.getCategory().getId()));
        assertThat(actual.get(0).getViews(), equalTo(event1Views));
        assertThat(actual.get(1).getId(), equalTo(event2.getId()));
        assertThat(actual.get(1).getInitiator().getId(), equalTo(event2.getInitiator().getId()));
        assertThat(actual.get(1).getCategory().getId(), equalTo(event2.getCategory().getId()));
        assertThat(actual.get(1).getViews(), equalTo(event2Views));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение update
     */
    @Test
    void shouldUpdate() {
        List<Long> eventIds = List.of(event1.getId());
        Long event1Views = 5L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views));
        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("New annotation")
                .category(category.getId())
                .description("New description")
                .eventDate(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .location(new Location(0., 2.))
                .paid(true)
                .participantLimit(50)
                .requestModeration(true)
                .title("New title")
                .eventId(event1.getId())
                .build();

        PrivateEventFullDto actual = privateEventService.update(user1.getId(), newEventDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getInitiator().getId(), equalTo(user1.getId()));
        assertThat(actual.getCategory().getId(), equalTo(newEventDto.getCategory()));
        assertThat(actual.getAnnotation(), equalTo(newEventDto.getAnnotation()));
        assertThat(actual.getDescription(), equalTo(newEventDto.getDescription()));
        assertThat(actual.getEventDate(), equalTo(ExploreDateFormatter.format(newEventDto.getEventDate())));
        assertThat(actual.getAnnotation(), equalTo(newEventDto.getAnnotation()));
        assertThat(actual.getLocation(), equalTo(newEventDto.getLocation()));
        assertThat(actual.getParticipantLimit(), equalTo(newEventDto.getParticipantLimit()));
        assertThat(actual.getRequestModeration(), equalTo(newEventDto.getRequestModeration()));
        assertThat(actual.getTitle(), equalTo(newEventDto.getTitle()));
        assertThat(actual.getId(), equalTo(newEventDto.getEventId()));
        assertThat(actual.getViews(), equalTo(event1Views));
        assertThat(actual.getConfirmedRequests(), equalTo(0L));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("New annotation")
                .category(category.getId())
                .description("New description")
                .eventDate(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .location(new Location(0., 2.))
                .paid(true)
                .participantLimit(50)
                .requestModeration(true)
                .title("New title")
                .build();

        PrivateEventFullDto actual = privateEventService.add(user1.getId(), newEventDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getInitiator().getId(), equalTo(user1.getId()));
        assertThat(actual.getCategory().getId(), equalTo(newEventDto.getCategory()));
        assertThat(actual.getAnnotation(), equalTo(newEventDto.getAnnotation()));
        assertThat(actual.getDescription(), equalTo(newEventDto.getDescription()));
        assertThat(actual.getEventDate(), equalTo(ExploreDateFormatter.format(newEventDto.getEventDate())));
        assertThat(actual.getAnnotation(), equalTo(newEventDto.getAnnotation()));
        assertThat(actual.getLocation(), equalTo(newEventDto.getLocation()));
        assertThat(actual.getParticipantLimit(), equalTo(newEventDto.getParticipantLimit()));
        assertThat(actual.getRequestModeration(), equalTo(newEventDto.getRequestModeration()));
        assertThat(actual.getTitle(), equalTo(newEventDto.getTitle()));
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getViews(), equalTo(0L));
        assertThat(actual.getConfirmedRequests(), equalTo(0L));
        assertThat(actual.getCreatedOn(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.getState(), equalTo(EventState.PENDING));
    }

    /**
     * Стандартное поведение findById
     */
    @Test
    void shouldFindById() {
        List<Long> eventIds = List.of(event1.getId());
        Long event1Views = 5L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views));

        PrivateEventFullDto actual = privateEventService.findById(user1.getId(), event1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getInitiator().getId(), equalTo(user1.getId()));
        assertThat(actual.getCategory().getId(), equalTo(category.getId()));
        assertThat(actual.getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.getDescription(), equalTo(event1.getDescription()));
        assertThat(actual.getEventDate(), equalTo(ExploreDateFormatter.format(event1.getEventDate())));
        assertThat(actual.getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.getLocation(), equalTo(event1.getLocation()));
        assertThat(actual.getParticipantLimit(), equalTo(event1.getParticipantLimit()));
        assertThat(actual.getRequestModeration(), equalTo(event1.getRequestModeration()));
        assertThat(actual.getTitle(), equalTo(event1.getTitle()));
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getViews(), equalTo(event1Views));
        assertThat(actual.getConfirmedRequests(), equalTo(0L));
        assertThat(actual.getState(), equalTo(event1.getEventState()));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение cancel
     */
    @Test
    void shouldCancel() {
        List<Long> eventIds = List.of(event1.getId());
        Long event1Views = 5L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views));
        event1.setEventState(EventState.PENDING);

        PrivateEventFullDto actual = privateEventService.cancel(user1.getId(), event1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getInitiator().getId(), equalTo(user1.getId()));
        assertThat(actual.getCategory().getId(), equalTo(category.getId()));
        assertThat(actual.getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.getDescription(), equalTo(event1.getDescription()));
        assertThat(actual.getEventDate(), equalTo(ExploreDateFormatter.format(event1.getEventDate())));
        assertThat(actual.getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.getLocation(), equalTo(event1.getLocation()));
        assertThat(actual.getParticipantLimit(), equalTo(event1.getParticipantLimit()));
        assertThat(actual.getRequestModeration(), equalTo(event1.getRequestModeration()));
        assertThat(actual.getTitle(), equalTo(event1.getTitle()));
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getViews(), equalTo(event1Views));
        assertThat(actual.getConfirmedRequests(), equalTo(0L));
        assertThat(actual.getState(), equalTo(EventState.CANCELED));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Ожидаем ConditionsNotMetException при попытке отменить события со статусом CANCELED
     */
    @Test
    void shouldNotCancelCanceledEvent() {
        List<Long> eventIds = List.of(event1.getId());
        Long event1Views = 5L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views));

         assertThrows(ConditionsNotMetException.class, () -> privateEventService.cancel(user1.getId(), event1.getId()));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Ожидаем EventNotFoundException при попытке отменить не существующее событие
     */
    @Test
    void shouldThrowNotFoundWhenCancelWithIncorrectId() {
        Long incorrectId = -1L;

        assertThrows(EventNotFoundException.class, () -> privateEventService.cancel(user1.getId(), incorrectId));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClient);
    }
}