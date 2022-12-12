package ru.practicum.explorewithme.apiprivate.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.apiprivate.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateRequestService;
import ru.practicum.explorewithme.base.exception.RequestNotFoundException;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.Request;
import ru.practicum.explorewithme.base.model.RequestState;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.repository.CategoryRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;
import ru.practicum.explorewithme.base.repository.RequestRepository;
import ru.practicum.explorewithme.base.repository.UserRepository;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Интеграционные тесты PrivateRequestServiceImpl
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PrivateRequestServiceImpl.class, ExploreDateFormatter.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PrivateRequestServiceImplTest {
    final UserRepository userRepository;
    final EventRepository eventRepository;
    final CategoryRepository categoryRepository;
    final RequestRepository requestRepository;
    final PrivateRequestService privateRequestService;

    User user1;
    User user2;
    Category category;
    Event event1;
    Event event2;
    Request request1;

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
                .initiator(user2)
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
        request1 = Request.builder()
                .requester(user2)
                .event(event1)
                .requestState(RequestState.PENDING)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        requestRepository.save(request1);
    }

    /**
     * Стандартное поведение find
     */
    @Test
    void shouldFind() {
        List<ParticipationRequestDto> actual = privateRequestService.find(user1.getId(), event1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getCreated(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.get(0).getEvent(), equalTo(event1.getId()));
        assertThat(actual.get(0).getRequester(), equalTo(user2.getId()));
        assertThat(actual.get(0).getStatus(), equalTo(RequestState.PENDING));
    }

    /**
     * Стандартное поведение confirm
     */
    @Test
    void shouldConfirm() {
        ParticipationRequestDto actual = privateRequestService.confirm(user1.getId(), event1.getId(), request1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getCreated(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.getEvent(), equalTo(event1.getId()));
        assertThat(actual.getRequester(), equalTo(user2.getId()));
        assertThat(actual.getStatus(), equalTo(RequestState.CONFIRMED));
    }

    /**
     * Ожидаем RequestNotFoundException при попытке подтвердить несуществующий запрос
     */
    @Test
    void shouldThrowNotFoundWhenIncorrectId() {
        assertThrows(RequestNotFoundException.class,
                () -> privateRequestService.confirm(user1.getId(), event1.getId(), -1L));
    }

    /**
     * Стандартное поведение reject
     */
    @Test
    void shouldReject() {
        ParticipationRequestDto actual = privateRequestService.reject(user1.getId(), event1.getId(), request1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getCreated(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.getEvent(), equalTo(event1.getId()));
        assertThat(actual.getRequester(), equalTo(user2.getId()));
        assertThat(actual.getStatus(), equalTo(RequestState.REJECTED));
    }

    /**
     * Стандартное поведение findByUserId
     */
    @Test
    void shouldFindByUserId() {
        List<ParticipationRequestDto> actual = privateRequestService.findByUserId(user2.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getCreated(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.get(0).getEvent(), equalTo(event1.getId()));
        assertThat(actual.get(0).getRequester(), equalTo(user2.getId()));
        assertThat(actual.get(0).getStatus(), equalTo(RequestState.PENDING));
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        ParticipationRequestDto actual = privateRequestService.add(user1.getId(), event2.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getCreated(), equalTo(ExploreDateFormatter.format(LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS))));
        assertThat(actual.getEvent(), equalTo(event2.getId()));
        assertThat(actual.getRequester(), equalTo(user1.getId()));
        assertThat(actual.getStatus(), equalTo(RequestState.CONFIRMED));
    }
}