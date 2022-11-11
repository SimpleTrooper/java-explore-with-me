package ru.practicum.explorewithme.apiprivate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер приватного API - события
 */
@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
}
