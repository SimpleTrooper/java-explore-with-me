package ru.practicum.explorewithme.apipublic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO подборки событий - public API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicCompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class EventShortDto {
        private String annotation;
        private CategoryDto category;
        private Integer confirmedRequests;
        private LocalDateTime eventDate;
        private Long id;
        private UserShortDto initiator;
        private Boolean paid;
        private String title;
        private Integer views;

        @Getter
        @Setter
        @AllArgsConstructor
        @RequiredArgsConstructor
        static class CategoryDto {
            private Long id;
            private String name;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @RequiredArgsConstructor
        static class UserShortDto {
            private Long id;
            private String name;
        }
    }
}
