package ru.practicum.explorewithme.apipublic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventWithViews;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO подборки событий - public API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicCompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EventShortDto {
        private String annotation;
        private CategoryDto category;
        private Long confirmedRequests;
        private String eventDate;
        private Long id;
        private UserShortDto initiator;
        private Boolean paid;
        private String title;
        private Long views;

        @Getter
        @Setter
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class CategoryDto {
            private Long id;
            private String name;

            public static CategoryDto from(Category category) {
                return new CategoryDto(category.getId(), category.getName());
            }
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class UserShortDto {
            private Long id;
            private String name;

            public static UserShortDto from(User user) {
                return new UserShortDto(user.getId(), user.getUsername());
            }
        }

        public static EventShortDto from(Event event, Long views) {
            return EventShortDto.builder()
                    .annotation(event.getAnnotation())
                    .category(CategoryDto.from(event.getCategory()))
                    .confirmedRequests(event.getConfirmedRequests())
                    .eventDate(ExploreDateFormatter.format(event.getEventDate()))
                    .id(event.getId())
                    .initiator(UserShortDto.from(event.getInitiator()))
                    .paid(event.getPaid())
                    .title(event.getTitle())
                    .views(views)
                    .build();
        }
    }

    public static PublicCompilationDto from(Compilation compilation, List<EventWithViews> events) {
        List<EventShortDto> shortDtoEvents = events.stream()
                .map(event -> EventShortDto.from(event.getEvent(), event.getViews()))
                .collect(Collectors.toList());
        return PublicCompilationDto.builder()
                .events(shortDtoEvents)
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
