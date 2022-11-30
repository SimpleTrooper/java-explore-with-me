package ru.practicum.explorewithme.apiprivate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.util.ExploreDateFormatter;

/**
 * Короткое DTO события - private API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateEventShortDto {
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
    @NoArgsConstructor
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
    @NoArgsConstructor
    public static class UserShortDto {
        private Long id;
        private String name;

        public static UserShortDto from(User user) {
            return new UserShortDto(user.getId(), user.getUsername());
        }
    }

    public static PrivateEventShortDto from(Event event, Long views) {
        return PrivateEventShortDto.builder()
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
