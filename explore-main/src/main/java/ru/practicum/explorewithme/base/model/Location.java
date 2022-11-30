package ru.practicum.explorewithme.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Сущность локации
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "location_name")
    private String name;

    @Column(name = "location_description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "location_type")
    private LocationType type;

    @Column(name = "location_country")
    private String country;

    @Column(name = "location_state")
    private String state;

    @Column(name = "location_city")
    private String city;

    @Column(name = "location_street")
    private String street;

    @Column(name = "location_housenumber")
    private String houseNumber;

    @Column(name = "location_postcode")
    private String postcode;

    @Column(name = "resolved")
    private Boolean resolved;

    @Column(name = "resolve_date")
    private LocalDateTime resolveDate;
}
