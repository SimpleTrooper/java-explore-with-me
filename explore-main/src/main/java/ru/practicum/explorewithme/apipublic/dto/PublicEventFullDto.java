package ru.practicum.explorewithme.apipublic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Category;
import ru.practicum.explorewithme.base.model.Event;
import ru.practicum.explorewithme.base.model.EventState;
import ru.practicum.explorewithme.base.model.LocationCoordinates;
import ru.practicum.explorewithme.base.model.User;
import ru.practicum.util.ExploreDateFormatter;

/**
 * Полное DTO события - public API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicEventFullDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationCoordinates location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
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

    public static PublicEventFullDto from(Event event, Long views) {
        return PublicEventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDto.from(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(ExploreDateFormatter.format(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(ExploreDateFormatter.format(event.getEventDate()))
                .id(event.getId())
                .initiator(UserShortDto.from(event.getInitiator()))
                .location(event.getLocationCoordinates())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(ExploreDateFormatter.format(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getEventState())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
