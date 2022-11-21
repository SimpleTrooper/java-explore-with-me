package ru.practicum.explorewithme.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.admin.dto.AdminCompilationDto;
import ru.practicum.explorewithme.admin.dto.NewCompilationDto;
import ru.practicum.explorewithme.admin.service.AdminCompilationService;
import ru.practicum.explorewithme.base.exception.CompilationNotFoundException;
import ru.practicum.explorewithme.base.exception.EventNotFoundException;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.repository.CompilationRepository;
import ru.practicum.explorewithme.base.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики подборки событий
 */
@Service
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public AdminCompilationServiceImpl(CompilationRepository compilationRepository,
                                       EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public AdminCompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation newCompilation = NewCompilationDto.toCompilation(newCompilationDto);
        List<Long> eventIds = newCompilationDto.getEvents();
        List<EventWithReqAndViews> compilationEventsWithViews = new ArrayList<>();
        if (eventIds != null) {
            compilationEventsWithViews = eventRepository.findAllByIdInWithViews(eventIds);
            if (compilationEventsWithViews.size() != eventIds.size()) {
                throw new EventNotFoundException("Event in compilation is not found");
            }
            List<Event> compilationEvents = compilationEventsWithViews.stream()
                    .map(EventWithReqAndViews::getEvent)
                    .collect(Collectors.toList());
            newCompilation.setEvents(compilationEvents);
        }

        compilationRepository.save(newCompilation);
        return AdminCompilationDto.from(newCompilation, compilationEventsWithViews);
    }

    @Override
    @Transactional
    public void delete(Long compilationId) {
        findByIdOrThrow(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        Compilation compilation = findByIdOrThrow(compilationId);
        Event event = findEventByIdOrThrow(eventId);
        compilation.getEvents().remove(event);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compilationId, Long eventId) {
        Compilation compilation = findByIdOrThrow(compilationId);
        Event event = findEventByIdOrThrow(eventId);
        compilation.getEvents().add(event);
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compilationId) {
        Compilation compilation = findByIdOrThrow(compilationId);
        compilation.setPinned(false);
    }

    @Override
    @Transactional
    public void pinCompilation(Long compilationId) {
        Compilation compilation = findByIdOrThrow(compilationId);
        compilation.setPinned(true);
    }

    private Compilation findByIdOrThrow(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(String.format("Compilation with id = %d " +
                        "is not found", compilationId)));
    }

    private Event findEventByIdOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id = %d " +
                        "is not found", eventId)));
    }
}
