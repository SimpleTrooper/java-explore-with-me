package ru.practicum.explorewithme.apipublic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.apipublic.dto.CompilationDto;

import java.util.List;

/**
 * Контроллер публичного API - подборки событий
 */
@Slf4j
@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {
    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }
    @GetMapping("/{compilationId}")
    public CompilationDto findById(@PathVariable Long compilationId) {
        return null;
    }
}
