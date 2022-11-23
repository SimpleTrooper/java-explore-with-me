package ru.practicum.explorewithme.apiprivate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.apiprivate.dto.NewEventDto;
import ru.practicum.explorewithme.apiprivate.dto.PrivateEventFullDto;
import ru.practicum.explorewithme.apiprivate.service.PrivateEventService;
import ru.practicum.explorewithme.apiprivate.service.PrivateRequestService;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

/**
 * Тесты для PrivateEventController
 */
@AutoConfigureMockMvc
@WebMvcTest(controllers = PrivateEventController.class)
@Import(ExploreDateFormatter.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PrivateEventControllerTest {
    final MockMvc mockMvc;
    @MockBean
    PrivateEventService privateEventService;
    @MockBean
    PrivateRequestService privateRequestService;
    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Стандартное поведение add - корректное время начала события
     * @throws Exception
     */
    @Test
    void shouldValidateEventTime3HourAfterNow() throws Exception {
        long userId = 1L;
        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("New annotation")
                .category(1L)
                .description("New description")
                .eventDate(LocalDateTime.now().plusHours(3).truncatedTo(ChronoUnit.SECONDS))
                .location(new Location(0.0, 0.0))
                .paid(false)
                .participantLimit(10)
                .requestModeration(false)
                .title("New title")
                .build();
        PrivateEventFullDto expected = PrivateEventFullDto.builder()
                .id(1L)
                .annotation(newEventDto.getAnnotation())
                .category(new PrivateEventFullDto.CategoryDto(1L, "Category"))
                .description(newEventDto.getDescription())
                .eventDate(ExploreDateFormatter.format(newEventDto.getEventDate()))
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .initiator(new PrivateEventFullDto.UserShortDto(userId, "Username"))
                .build();
        Mockito.when(privateEventService.add(anyLong(), any())).thenReturn(expected);

        mockMvc.perform(post("/users/" + userId + "/events")
                        .content(objectMapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expected.getId()), Long.class))
                .andExpect(jsonPath("$.annotation", is(expected.getAnnotation())))
                .andExpect(jsonPath("$.category.id", is(expected.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.description", is(expected.getDescription())))
                .andExpect(jsonPath("$.eventDate", is(expected.getEventDate())))
                .andExpect(jsonPath("$.location.lat", is(expected.getLocation().getLat())))
                .andExpect(jsonPath("$.paid", is(expected.getPaid())))
                .andExpect(jsonPath("$.participantLimit", is(expected.getParticipantLimit())))
                .andExpect(jsonPath("$.requestModeration", is(expected.getRequestModeration())))
                .andExpect(jsonPath("$.title", is(expected.getTitle())))
                .andExpect(jsonPath("$.initiator.id", is(expected.getInitiator().getId()), Long.class));

        verify(privateEventService, times(1)).add(anyLong(), any());
    }

    /**
     * Событие со временем начала +1 час от текущего не должно пройти валидацию
     * @throws Exception
     */
    @Test
    void shouldNotValidateEventTime1HourAfterNow() throws Exception {
        long userId = 1L;
        String exceptionReason = "Validation error.";
        String errorMessage = "eventDate: Event date must be at least 2 hours from now";
        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("New annotation")
                .category(1L)
                .description("New description")
                .eventDate(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
                .location(new Location(0.0, 0.0))
                .paid(false)
                .participantLimit(10)
                .requestModeration(false)
                .title("New title")
                .build();

        mockMvc.perform(post("/users/" + userId + "/events")
                        .content(objectMapper.writeValueAsString(newEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason", is(exceptionReason)))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(privateEventService);
    }
}