package ru.practicum.explorewithme.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Compilation;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventWithReqAndViews;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.explorewithme.base.util.ExploreDateFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO подборки событий - admin API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminCompilationDto {
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

        public static EventShortDto from(Event event, Long confirmedRequests, Long views) {

            return EventShortDto.builder()
                    .annotation(event.getAnnotation())
                    .category(CategoryDto.from(event.getCategory()))
                    .confirmedRequests(confirmedRequests)
                    .eventDate(ExploreDateFormatter.format(event.getEventDate()))
                    .id(event.getId())
                    .initiator(UserShortDto.from(event.getInitiator()))
                    .paid(event.getPaid())
                    .title(event.getTitle())
                    .views(views)
                    .build();
        }
    }

    public static AdminCompilationDto from(Compilation compilation,
                                           List<EventWithReqAndViews> compilationEventsWithViews) {
        List<EventShortDto> events = new ArrayList<>();
        if (compilationEventsWithViews != null) {
            compilationEventsWithViews.forEach(event -> events.add(EventShortDto.from(event.getEvent(),
                    event.getConfirmedRequests(),
                    event.getViews())));
        }
        return AdminCompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }
}
