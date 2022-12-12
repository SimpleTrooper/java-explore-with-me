package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreclient.ExploreClientForStats;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.explorewithme.apipublic.controller.request.PublicGetEventsRequest;
import ru.practicum.explorewithme.apipublic.controller.request.SortType;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;
import ru.practicum.explorewithme.apipublic.service.PublicEventService;
import ru.practicum.explorewithme.base.handler.HandleExceptions;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Контроллер публичного API - события
 */
@Slf4j
@HandleExceptions
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService publicEventService;
    private final ExploreClientForStats exploreClientForStats;

    @Value("${explore.app.id}")
    private String exploreAppId;

    @Autowired
    public PublicEventController(PublicEventService publicEventService,
                                 ExploreClientForStats exploreClientForStats) {
        this.publicEventService = publicEventService;
        this.exploreClientForStats = exploreClientForStats;
    }

    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param location      id локации, в которой будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param sort          только события у которых не исчерпан лимит запросов на участие
     * @param onlyAvailable вариант сортировки: по дате события или по количеству просмотров
     * @param from          пагинация - начиная с
     * @param size          пагинация - количество записей
     * @return Список коротких DTO событий
     */
    @GetMapping
    public List<PublicEventShortDto> findAll(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) Long location,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false) LocalDateTime rangeStart,
                                             @RequestParam(required = false) LocalDateTime rangeEnd,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HttpServletRequest httpServletRequest) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(exploreAppId)
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        exploreClientForStats.hit(endpointHit);

        log.info("Public request to find all events");
        PublicGetEventsRequest publicGetEventsRequest = PublicGetEventsRequest.builder()
                .text(text)
                .categories(categories)
                .location(location)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(SortType.from(sort))
                .build();
        return publicEventService.findAll(publicGetEventsRequest, new PaginationRequest(from, size));
    }

    /**
     * Поиск события по id
     *
     * @param eventId id события
     * @return Полный DTO события
     */
    @GetMapping("/{eventId}")
    public PublicEventFullDto findById(@PathVariable Long eventId,
                                       HttpServletRequest httpServletRequest) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(exploreAppId)
                .uri(httpServletRequest.getRequestURI())
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        exploreClientForStats.hit(endpointHit);
        log.info("Public request to find event with id = {}", eventId);
        return publicEventService.findById(eventId);
    }
}
