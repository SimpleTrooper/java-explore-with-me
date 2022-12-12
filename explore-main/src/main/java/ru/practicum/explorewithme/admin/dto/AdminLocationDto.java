package ru.practicum.explorewithme.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.util.ExploreDateFormatter;

/**
 * DTO локации - admin API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminLocationDto {
    private Long id;
    private String name;
    private String description;
    private Double lat;
    private Double lon;
    private Double radius;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String street;
    private String houseNumber;
    private String type;
    private Boolean resolved;
    private String resolveDate;

    public static AdminLocationDto from(Location location) {
        return AdminLocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .country(location.getCountry())
                .state(location.getState())
                .city(location.getCity())
                .postalCode(location.getPostcode())
                .street(location.getStreet())
                .type(location.getType().toString())
                .houseNumber(location.getHouseNumber())
                .resolved(location.getResolved())
                .resolveDate(ExploreDateFormatter.format(location.getResolveDate()))
                .build();
    }
}
