package ru.practicum.explorewithme.apipublic.service.impl;

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
import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.controller.request.SortType;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.apipublic.service.PublicEventService;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.LocationRepository;
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
 * Интеграционные тесты для PublicEventServiceImpl
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PublicEventServiceImpl.class, ExploreDateFormatter.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PublicEventServiceImplTest {
    @MockBean
    final ExploreClientForStats exploreClientForStats;
    @InjectMocks
    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final RequestRepository requestRepository;
    final LocationRepository locationRepository;
    final PublicEventService publicEventService;

    User user1;
    User user2;
    Category category;
    Event event1;
    Event event2;
    Event event3;
    Request request;

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
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .build();
        event2 = Event.builder()
                .title("Test event 2")
                .annotation("Test event 2 annotation")
                .description("Test event 2 description")
                .category(category)
                .initiator(user2)
                .paid(false)
                .participantLimit(1)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(1L)
                .build();
        event3 = Event.builder()
                .title("Test event 3")
                .annotation("Test event 3 annotation")
                .description("Test event 3 description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .build();

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        request = Request.builder()
                .requester(user1)
                .event(event2)
                .requestState(RequestState.CONFIRMED)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        requestRepository.save(request);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId(), event3.getId());
        Long event1Views = 5L;
        Long event2Views = 3L;
        Long event3Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views,
                        event3.getId(), event3Views));
        PublicGetEventsRequest publicGetEventsRequest = PublicGetEventsRequest.builder()
                .text("annotation")
                .categories(List.of(category.getId()))
                .paid(false)
                .rangeStart(LocalDateTime.now().minusDays(10))
                .rangeEnd(LocalDateTime.now().plusDays(10))
                .onlyAvailable(false)
                .sort(SortType.VIEWS)
                .build();

        List<PublicEventShortDto> actual = publicEventService.findAll(publicGetEventsRequest,
                new PaginationRequest(0, 3));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(eventIds.size()));
        assertThat(actual.get(0).getId(), equalTo(event3.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(event3.getTitle()));
        assertThat(actual.get(0).getAnnotation(), equalTo(event3.getAnnotation()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(event3.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(event3.getCategory().getId()));
        assertThat(actual.get(0).getConfirmedRequests(), equalTo(0L));
        assertThat(actual.get(0).getViews(), equalTo(event3Views));
        assertThat(actual.get(1).getId(), equalTo(event1.getId()));
        assertThat(actual.get(1).getTitle(), equalTo(event1.getTitle()));
        assertThat(actual.get(1).getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.get(1).getInitiator().getId(), equalTo(event1.getInitiator().getId()));
        assertThat(actual.get(1).getCategory().getId(), equalTo(event1.getCategory().getId()));
        assertThat(actual.get(1).getConfirmedRequests(), equalTo(0L));
        assertThat(actual.get(1).getViews(), equalTo(event1Views));
        assertThat(actual.get(2).getId(), equalTo(event2.getId()));
        assertThat(actual.get(2).getTitle(), equalTo(event2.getTitle()));
        assertThat(actual.get(2).getAnnotation(), equalTo(event2.getAnnotation()));
        assertThat(actual.get(2).getInitiator().getId(), equalTo(event2.getInitiator().getId()));
        assertThat(actual.get(2).getCategory().getId(), equalTo(event2.getCategory().getId()));
        assertThat(actual.get(2).getConfirmedRequests(), equalTo(1L));
        assertThat(actual.get(2).getViews(), equalTo(event2Views));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение findAll onlyAvailable = true
     */
    @Test
    void shouldFindAllAvailable() {
        List<Long> eventIds = List.of(event1.getId(), event3.getId());
        Long event1Views = 5L;
        Long event3Views = 10L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event3.getId(), event3Views));
        PublicGetEventsRequest publicGetEventsRequest = PublicGetEventsRequest.builder()
                .text("annotation")
                .categories(List.of(category.getId()))
                .paid(false)
                .rangeStart(LocalDateTime.now().minusDays(10))
                .rangeEnd(LocalDateTime.now().plusDays(10))
                .onlyAvailable(true)
                .sort(SortType.VIEWS)
                .build();

        List<PublicEventShortDto> actual = publicEventService.findAll(publicGetEventsRequest,
                new PaginationRequest(0, 2));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(eventIds.size()));
        assertThat(actual.get(0).getId(), equalTo(event3.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(event3.getTitle()));
        assertThat(actual.get(0).getAnnotation(), equalTo(event3.getAnnotation()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(event3.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(event3.getCategory().getId()));
        assertThat(actual.get(1).getId(), equalTo(event1.getId()));
        assertThat(actual.get(1).getTitle(), equalTo(event1.getTitle()));
        assertThat(actual.get(1).getAnnotation(), equalTo(event1.getAnnotation()));
        assertThat(actual.get(1).getInitiator().getId(), equalTo(event1.getInitiator().getId()));
        assertThat(actual.get(1).getCategory().getId(), equalTo(event1.getCategory().getId()));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение findById
     */
    @Test
    void shouldFindById() {
        List<Long> eventIds = List.of(event2.getId());
        Long event2Views = 3L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event2.getId(), event2Views));

        PublicEventFullDto actual = publicEventService.findById(event2.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(event2.getId()));
        assertThat(actual.getTitle(), equalTo(event2.getTitle()));
        assertThat(actual.getAnnotation(), equalTo(event2.getAnnotation()));
        assertThat(actual.getInitiator().getId(), equalTo(event2.getInitiator().getId()));
        assertThat(actual.getCategory().getId(), equalTo(event2.getCategory().getId()));
        assertThat(actual.getConfirmedRequests(), equalTo(1L));
        assertThat(actual.getViews(), equalTo(event2Views));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClientForStats);
    }
}