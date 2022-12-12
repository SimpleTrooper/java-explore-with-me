package ru.practicum.explorewithme.admin.service.impl;

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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.ExploreClientForStats;
import ru.practicum.explorewithme.admin.controller.request.AdminGetEventsRequest;
import ru.practicum.explorewithme.admin.dto.AdminEventFullDto;
import ru.practicum.explorewithme.admin.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.admin.service.AdminEventService;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.RequestRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Интеграционные тесты для AdminEventServiceImpl
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AdminEventServiceImpl.class, ExploreDateFormatter.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminEventServiceImplTest {
    @MockBean
    final ExploreClientForStats exploreClientForStats;
    @InjectMocks
    final EventRepository eventRepository;
    final RequestRepository requestRepository;
    final CategoryRepository categoryRepository;
    final UserRepository userRepository;
    final AdminEventService adminEventService;

    Event event1;
    Event event2;
    Event event3;
    Category category1;
    Category category2;
    User user1;
    User user2;

    @BeforeEach
    void init() {
        user1 = new User(null, "User 1", "mail1@mail.com");
        user2 = new User(null, "User 2", "mail2@mail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        category1 = new Category(null, "Category 1");
        category2 = new Category(null, "Category 2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        event1 = Event.builder()
                .title("Test event 1")
                .annotation("Test event 1 annotation")
                .description("Test event 1 description")
                .category(category1)
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
                .category(category1)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.CANCELED)
                .confirmedRequests(0L)
                .build();
        event3 = Event.builder()
                .title("Test event 3")
                .annotation("Test event 3 annotation")
                .description("Test event 3 description")
                .category(category2)
                .initiator(user2)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(4).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PENDING)
                .confirmedRequests(0L)
                .build();
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
    }

    /**
     * Стандартное поведение findAll - задействуем все параметры GetEventRequest
     */
    @Test
    void shouldFindAll() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId());
        Long event1Views = 5L;
        Long event2Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views));
        AdminGetEventsRequest getEventsRequest = AdminGetEventsRequest.builder()
                .users(List.of(user1.getId()))
                .categories(List.of(category1.getId()))
                .states(List.of(EventState.CANCELED))
                .rangeStart(LocalDateTime.now().minusDays(1))
                .rangeEnd(LocalDateTime.now().plusDays(4))
                .build();

        List<AdminEventFullDto> actual = adminEventService.findAll(getEventsRequest,
                new PaginationRequest(0, 10));

        assertThat(actual.size(), equalTo(eventIds.size()));
        assertThat(actual.get(0).getId(), equalTo(event1.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(event1.getTitle()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(event1.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(event1.getCategory().getId()));
        assertThat(actual.get(1).getId(), equalTo(event2.getId()));
        assertThat(actual.get(1).getTitle(), equalTo(event2.getTitle()));
        assertThat(actual.get(1).getInitiator().getId(), equalTo(event2.getInitiator().getId()));
        assertThat(actual.get(1).getCategory().getId(), equalTo(event2.getCategory().getId()));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение update
     */
    @Test
    void shouldUpdate() {
        Integer participantLimit = 10;
        List<Long> eventIds = List.of(event1.getId());
        Long event1Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views));
        AdminUpdateEventRequest adminUpdateEventRequest = AdminUpdateEventRequest.builder()
                .title("Updated title")
                .paid(true)
                .requestModeration(true)
                .eventDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .participantLimit(participantLimit)
                .category(category2.getId())
                .build();

        AdminEventFullDto actual = adminEventService.update(event1.getId(), adminUpdateEventRequest);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(event1.getId()));
        assertThat(actual.getTitle(), equalTo(adminUpdateEventRequest.getTitle()));
        assertThat(actual.getPaid(), equalTo(adminUpdateEventRequest.getPaid()));
        assertThat(actual.getRequestModeration(), equalTo(adminUpdateEventRequest.getRequestModeration()));
        assertThat(actual.getEventDate(), equalTo(ExploreDateFormatter.format(adminUpdateEventRequest.getEventDate())));
        assertThat(actual.getParticipantLimit(), equalTo(adminUpdateEventRequest.getParticipantLimit()));
        assertThat(actual.getCategory().getId(), equalTo(adminUpdateEventRequest.getCategory()));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение publish
     */
    @Test
    void shouldPublish() {
        List<Long> eventIds = List.of(event3.getId());
        Long event3Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event3.getId(), event3Views));

        AdminEventFullDto actual = adminEventService.publish(event3.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(event3.getId()));
        assertThat(actual.getPublishedOn(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.getTitle(), equalTo(event3.getTitle()));
        assertThat(actual.getState(), equalTo(EventState.PUBLISHED));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение reject
     */
    @Test
    void shouldReject() {
        List<Long> eventIds = List.of(event3.getId());
        Long event3Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event3.getId(), event3Views));

        AdminEventFullDto actual = adminEventService.reject(event3.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(event3.getId()));
        assertThat(actual.getTitle(), equalTo(event3.getTitle()));
        assertThat(actual.getState(), equalTo(EventState.CANCELED));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClientForStats);
    }
}