package ru.practicum.explorewithme.apipublic.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.ExploreClientForStats;
import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.controller.request.SortType;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.apipublic.service.PublicEventService;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.LocationCoordinates;
import ru.practicum.explorewithme.base.model.LocationType;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.LocationRepository;
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
 * Интеграционные тесты для PublicLocationServiceImpl - функциональность "локации"
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PublicEventServiceImpl.class, ExploreDateFormatter.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PublicEventServiceImplLocationTest {
    @MockBean
    final ExploreClientForStats exploreClientForStats;
    @InjectMocks
    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final LocationRepository locationRepository;
    final PublicEventService publicEventService;

    User user1;
    Category category;
    Location location1;
    Location location2;
    Event eventWithLocation1;
    Event eventWithLocation2;
    Event eventInDzerzhinsk;
    Event eventNearDzerzhinsk;

    @BeforeEach
    void init() {
        user1 = new User(null, "User 1", "mail1@mail.com");
        userRepository.save(user1);

        category = new Category(null, "Category 1");
        categoryRepository.save(category);

        location1 = Location.builder()
                .lat(0.)
                .lon(0.)
                .radius(1.)
                .name("Null location")
                .description("Null location description")
                .type(LocationType.PLACE)
                .resolved(false)
                .build();
        location2 = Location.builder()
                .lat(56.236282190822)
                .lon(43.43878174180558)
                .radius(8.)
                .name("Дзержинск")
                .description("Город в Нижегородской области России")
                .type(LocationType.CITY)
                .resolved(false)
                .build();
        locationRepository.save(location1);
        locationRepository.save(location2);


        eventWithLocation1 = Event.builder()
                .title("Test event with location 1")
                .annotation("Test event with location 1 annotation")
                .description("Test event with location 1 description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .locationCoordinates(new LocationCoordinates(0., 0.))
                .build();

        eventWithLocation2 = Event.builder()
                .title("Test event with location 2")
                .annotation("Test event with location 2 annotation")
                .description("TTest event with location 2 description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .locationCoordinates(new LocationCoordinates(0., 0.))
                .build();

        eventInDzerzhinsk = Event.builder()
                .title("Test event in Dzerzhinsk")
                .annotation("Test event in Dzerzhinsk annotation")
                .description("Test event in Dzerzhinsk description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .locationCoordinates(new LocationCoordinates(56.24939691496344, 43.42755386000608))
                .build();

        eventNearDzerzhinsk = Event.builder()
                .title("Test event near Dzerzhinsk")
                .annotation("Test event near Dzerzhinsk annotation")
                .description("Test event near Dzerzhinsk description")
                .category(category)
                .initiator(user1)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .locationCoordinates(new LocationCoordinates(56.283372427950844, 43.53891611622154))
                .build();

        eventRepository.save(eventWithLocation1);
        eventRepository.save(eventWithLocation2);
        eventRepository.save(eventInDzerzhinsk);
        eventRepository.save(eventNearDzerzhinsk);
    }

    /**
     * Стандартное поведение findAll поиск по локации с нулевыми координатами
     */
    @Test
    void shouldFindAllInNullLocation() {
        List<Long> eventIds = List.of(eventWithLocation1.getId(), eventWithLocation2.getId());
        Long eventWithLocation1Views = 4L;
        Long eventWithLocation2Views = 15L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(eventWithLocation1.getId(), eventWithLocation1Views,
                        eventWithLocation2.getId(), eventWithLocation2Views));
        PublicGetEventsRequest publicGetEventsRequest = PublicGetEventsRequest.builder()
                .text("annotation")
                .categories(List.of(category.getId()))
                .location(location1.getId())
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
        assertThat(actual.get(0).getId(), equalTo(eventWithLocation2.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(eventWithLocation2.getTitle()));
        assertThat(actual.get(0).getAnnotation(), equalTo(eventWithLocation2.getAnnotation()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(eventWithLocation2.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(eventWithLocation2.getCategory().getId()));
        assertThat(actual.get(1).getId(), equalTo(eventWithLocation1.getId()));
        assertThat(actual.get(1).getTitle(), equalTo(eventWithLocation1.getTitle()));
        assertThat(actual.get(1).getAnnotation(), equalTo(eventWithLocation1.getAnnotation()));
        assertThat(actual.get(1).getInitiator().getId(), equalTo(eventWithLocation1.getInitiator().getId()));
        assertThat(actual.get(1).getCategory().getId(), equalTo(eventWithLocation1.getCategory().getId()));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение findAll поиск ивентов в Дзержинске, радиусом 8 км
     */
    @Test
    void shouldFindAllInDzerzhinskLocation() {
        List<Long> eventIds = List.of(eventInDzerzhinsk.getId());
        Long eventInDzerzhinskViews = 4L;
        Mockito.when(exploreClientForStats.getViewsForEvents(eventIds))
                .thenReturn(Map.of(eventInDzerzhinsk.getId(), eventInDzerzhinskViews));
        PublicGetEventsRequest publicGetEventsRequest = PublicGetEventsRequest.builder()
                .text("annotation")
                .categories(List.of(category.getId()))
                .location(location2.getId())
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
        assertThat(actual.get(0).getId(), equalTo(eventInDzerzhinsk.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(eventInDzerzhinsk.getTitle()));
        assertThat(actual.get(0).getAnnotation(), equalTo(eventInDzerzhinsk.getAnnotation()));
        assertThat(actual.get(0).getInitiator().getId(), equalTo(eventInDzerzhinsk.getInitiator().getId()));
        assertThat(actual.get(0).getCategory().getId(), equalTo(eventInDzerzhinsk.getCategory().getId()));

        verify(exploreClientForStats, times(1)).getViewsForEvents(eventIds);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClientForStats);
    }
}
