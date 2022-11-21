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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.explorewithme.apipublic.dto.PublicCompilationDto;
import ru.practicum.explorewithme.apipublic.service.PublicCompilationService;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.CompilationRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.RequestRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Интеграционные тесты для PublicCompilationServiceImpl
 */
@DataJpaTest
@Import(PublicCompilationServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PublicCompilationServiceImplTest {
    @MockBean
    final ExploreClient exploreClient;
    @InjectMocks
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final EventRepository eventRepository;
    final RequestRepository requestRepository;
    final CompilationRepository compilationRepository;
    final PublicCompilationService publicCompilationService;

    User user1;
    User user2;
    Category category;
    Event event1;
    Event event2;
    Request request;
    Compilation compilation1;
    Compilation compilation2;

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
                .build();
        event2 = Event.builder()
                .title("Test event 2")
                .annotation("Test event 2 annotation")
                .description("Test event 2 description")
                .category(category)
                .initiator(user2)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.CANCELED)
                .build();
        eventRepository.save(event1);
        eventRepository.save(event2);

        request = Request.builder()
                .requester(user1)
                .event(event2)
                .requestState(RequestState.CONFIRMED)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        requestRepository.save(request);

        List<Event> compilation1List = new ArrayList<>();
        List<Event> compilation2List = new ArrayList<>();
        compilation1List.add(event1);
        compilation1List.add(event2);
        compilation2List.add(event2);
        compilation1 = new Compilation(null, "Compilation 1", true, compilation1List);
        compilation2 = new Compilation(null, "Compilation 2", false, compilation2List);
        compilationRepository.save(compilation1);
        compilationRepository.save(compilation2);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId());
        Long event1Views = 5L;
        Long event2Views = 10L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views));

        List<PublicCompilationDto> actual = publicCompilationService.findAll(null,
                new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getId(), equalTo(compilation1.getId()));
        assertThat(actual.get(0).getTitle(), equalTo(compilation1.getTitle()));
        assertThat(actual.get(0).getPinned(), equalTo(compilation1.getPinned()));
        assertThat(actual.get(0).getEvents().get(0).getId(), equalTo(event1.getId()));
        assertThat(actual.get(0).getEvents().get(0).getViews(), equalTo(event1Views));
        assertThat(actual.get(0).getEvents().get(0).getConfirmedRequests(), equalTo(0L));
        assertThat(actual.get(0).getEvents().get(1).getId(), equalTo(event2.getId()));
        assertThat(actual.get(0).getEvents().get(1).getViews(), equalTo(event2Views));
        assertThat(actual.get(0).getEvents().get(1).getConfirmedRequests(), equalTo(1L));
        assertThat(actual.get(1).getId(), equalTo(compilation2.getId()));
        assertThat(actual.get(1).getTitle(), equalTo(compilation2.getTitle()));
        assertThat(actual.get(1).getPinned(), equalTo(compilation2.getPinned()));
        assertThat(actual.get(1).getEvents().get(0).getId(), equalTo(event2.getId()));
        assertThat(actual.get(1).getEvents().get(0).getViews(), equalTo(event2Views));
        assertThat(actual.get(1).getEvents().get(0).getConfirmedRequests(), equalTo(1L));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение findById
     */
    @Test
    void shouldFindById() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId());
        Long event1Views = 5L;
        Long event2Views = 10L;
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views));

        PublicCompilationDto actual = publicCompilationService.findById(compilation1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(compilation1.getId()));
        assertThat(actual.getTitle(), equalTo(compilation1.getTitle()));
        assertThat(actual.getPinned(), equalTo(compilation1.getPinned()));
        assertThat(actual.getEvents().get(0).getId(), equalTo(event1.getId()));
        assertThat(actual.getEvents().get(0).getViews(), equalTo(event1Views));
        assertThat(actual.getEvents().get(0).getConfirmedRequests(), equalTo(0L));
        assertThat(actual.getEvents().get(1).getId(), equalTo(event2.getId()));
        assertThat(actual.getEvents().get(1).getViews(), equalTo(event2Views));
        assertThat(actual.getEvents().get(1).getConfirmedRequests(), equalTo(1L));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClient);
    }
}