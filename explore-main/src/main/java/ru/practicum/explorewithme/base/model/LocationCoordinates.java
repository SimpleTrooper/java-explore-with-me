package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Локация события - долгота, широта.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LocationCoordinates {
    @Column(name = "location_lat")
    private Double lat;

    @Column(name = "location_lon")
    private Double lon;
}
