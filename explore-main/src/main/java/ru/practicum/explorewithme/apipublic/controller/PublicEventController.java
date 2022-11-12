package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.PublicEventFullDto;
import ru.practicum.explorewithme.apipublic.dto.PublicEventShortDto;

import java.util.List;

/**
 * Контроллер публичного API - события
 */
@Slf4j
@RestController
@RequestMapping("/events")
public class PublicEventController {
    @GetMapping
    public List<PublicEventShortDto> findAll() {
        return null;
    }

    @GetMapping("/eventId")
    public PublicEventFullDto findById() {
        return null;
    }
}
