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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.explorewithme.admin.dto.AdminCompilationDto;
import ru.practicum.explorewithme.admin.dto.NewCompilationDto;
import ru.practicum.explorewithme.admin.service.AdminCompilationService;
import ru.practicum.explorewithme.base.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.model.User;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DataJpaTest
@Import(AdminCompilationServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminCompilationServiceImplTest {
    @MockBean
    final ExploreClient exploreClient;
    final CompilationRepository compilationRepository;
    @InjectMocks
    final EventRepository eventRepository;
    final RequestRepository requestRepository;
    final CategoryRepository categoryRepository;
    final UserRepository userRepository;
    final AdminCompilationService adminCompilationService;

    Event event1;
    Event event2;
    Compilation compilation;
    Request request;
    Category category;
    User user;

    @BeforeEach
    void init() {
        user = new User(null, "Test username", "Test userEmail");
        userRepository.save(user);

        category = new Category(null, "Test category");
        categoryRepository.save(category);

        event1 = Event.builder()
                .title("Test event 1")
                .annotation("Test event 1 annotation")
                .description("Test event 1 description")
                .category(category)
                .initiator(user)
                .paid(false)
                .participantLimit(10)
                .eventDate(LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.CANCELED)
                .confirmedRequests(1L)
                .build();
        eventRepository.save(event1);

        event2 = Event.builder()
                .title("Test event 2")
                .annotation("Test event 2 annotation")
                .description("Test event 2 description")
                .category(category)
                .initiator(user)
                .paid(true)
                .participantLimit(15)
                .eventDate(LocalDateTime.now().plusDays(4).truncatedTo(ChronoUnit.SECONDS))
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestModeration(false)
                .eventState(EventState.PUBLISHED)
                .confirmedRequests(0L)
                .build();
        eventRepository.save(event2);

        List<Event> compilationEvents = new ArrayList<>();
        compilationEvents.add(event1);
        compilation = new Compilation(null, "Test compilation", true, compilationEvents);
        compilationRepository.save(compilation);
        request = Request.builder()
                .event(event1)
                .requester(user)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .requestState(RequestState.CONFIRMED)
                .build();
        requestRepository.save(request);
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        List<Long> eventIds = List.of(event1.getId(), event2.getId());
        Long event1Views = 5L;
        Long event2Views = 10L;
        NewCompilationDto newCompilationDto = new NewCompilationDto(eventIds,
                false, "New Compilation");
        Mockito.when(exploreClient.getViewsForEvents(eventIds))
                .thenReturn(Map.of(event1.getId(), event1Views, event2.getId(), event2Views));

        AdminCompilationDto actual = adminCompilationService.add(newCompilationDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getPinned(), equalTo(false));
        assertThat(actual.getEvents().size(), equalTo(newCompilationDto.getEvents().size()));
        assertThat(actual.getEvents().get(0).getId(), equalTo(newCompilationDto.getEvents().get(0)));
        assertThat(actual.getEvents().get(0).getCategory().getId(), equalTo(category.getId()));
        assertThat(actual.getEvents().get(0).getInitiator().getId(), equalTo(user.getId()));
        assertThat(actual.getEvents().get(0).getConfirmedRequests(), equalTo(1L));
        assertThat(actual.getEvents().get(0).getViews(), equalTo(event1Views));

        assertThat(actual.getEvents().get(1).getId(), equalTo(newCompilationDto.getEvents().get(1)));
        assertThat(actual.getEvents().get(1).getCategory().getId(), equalTo(category.getId()));
        assertThat(actual.getEvents().get(1).getInitiator().getId(), equalTo(user.getId()));
        assertThat(actual.getEvents().get(1).getViews(), equalTo(event2Views));

        verify(exploreClient, times(1)).getViewsForEvents(eventIds);
    }

    /**
     * Стандартное поведение delete
     */
    @Test
    void shouldDelete() {
        Long compilationId = compilation.getId();

        assertDoesNotThrow(() -> adminCompilationService.delete(compilationId));

        assertThrows(CompilationNotFoundException.class, () -> adminCompilationService.delete(compilationId));
    }

    /**
     * Стандартное поведение deleteEventFromCompilation
     */
    @Test
    void shouldDeleteEventFromCompilation() {
        Long compilationId = compilation.getId();
        Long eventId = event1.getId();
        int oldSize = compilationRepository.findById(compilationId).get().getEvents().size();
        assertDoesNotThrow(() -> adminCompilationService.deleteEventFromCompilation(compilationId, eventId));

        assertThat(compilationRepository.findById(compilationId).get().getEvents().size(),
                equalTo(oldSize - 1));
    }

    /**
     * Стандартное поведение addEventToCompilation
     */
    @Test
    void shouldAddEventToCompilation() {
        Long compilationId = compilation.getId();
        Long eventId = event2.getId();
        int oldSize = compilationRepository.findById(compilationId).get().getEvents().size();

        assertDoesNotThrow(() -> adminCompilationService.addEventToCompilation(compilationId, eventId));
        List<Event> actual = compilationRepository.findById(compilationId).get().getEvents();

        assertThat(actual.size(), equalTo(oldSize + 1));
        assertThat(actual.get(oldSize).getId(), equalTo(eventId));
    }

    /**
     * Стандартное поведение unpinCompilation
     */
    @Test
    void shouldUnpinCompilation() {
        compilation.setPinned(true);

        adminCompilationService.unpinCompilation(compilation.getId());
        Boolean actual = compilation.getPinned();

        assertThat(actual, equalTo(false));
    }

    /**
     * Стандартное поведение pinCompilation
     */
    @Test
    void shouldPinCompilation() {
        compilation.setPinned(false);

        adminCompilationService.pinCompilation(compilation.getId());
        Boolean actual = compilation.getPinned();

        assertThat(actual, equalTo(true));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(exploreClient);
    }
}