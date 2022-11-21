package ru.practicum.explorewithme.apipublic.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apipublic.dto.PublicCompilationDto;
import ru.practicum.explorewithme.apipublic.service.PublicCompilationService;
import ru.practicum.explorewithme.base.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.CompilationRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики подборок событий
 */
@Service
@Transactional
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public PublicCompilationServiceImpl(CompilationRepository compilationRepository,
                                        EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<PublicCompilationDto> findAll(Boolean pinned, PaginationRequest paginationRequest) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned,
                    paginationRequest.makeOffsetBasedByFieldAsc("id"));
        } else {
            compilations = compilationRepository.findAll(paginationRequest.makeOffsetBasedByFieldAsc("id")).toList();
        }

        Set<Long> eventIds = new HashSet<>();
        compilations.forEach(compilation -> compilation.getEvents().forEach(event -> eventIds.add(event.getId())));

        Map<Long, EventWithReqAndViews> eventsWithReqAndViews = eventRepository.findAllByIdInWithViews(eventIds)
                .stream().collect(Collectors.toMap(eventWithReqAndViews -> eventWithReqAndViews.getEvent().getId(),
                        eventWithReqAndViews -> eventWithReqAndViews));

        return compilations.stream()
                .map(compilation -> PublicCompilationDto.from(compilation,
                        compilation.getEvents().stream()
                                .map(event -> eventsWithReqAndViews.get(event.getId()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public PublicCompilationDto findById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(String.format("Compilation with id = %d " +
                        "is not found", compilationId)));

        List<Long> eventIds = new ArrayList<>();
        compilation.getEvents().forEach(event -> eventIds.add(event.getId()));

        List<EventWithReqAndViews> eventsWithReqAndViews = eventRepository.findAllByIdInWithViews(eventIds);

        return PublicCompilationDto.from(compilation, eventsWithReqAndViews);
    }
}
