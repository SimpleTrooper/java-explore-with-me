package ru.practicum.exploregeocoding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Информация с эндпоинта обратного геокодирования
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReverseGeocodeAnswer {
    private List<PlaceInformation> hits;
    private String locale;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceInformation {
        @JsonProperty("osm_id")
        private Long osmId;
        @JsonProperty("osm_type")
        private String osmType;
        @JsonProperty("osm_key")
        private String osmKey;
        private String name;
        private String country;
        private String state;
        private String city;
        private String postcode;
        private String street;
        private String housenumber;
    }
}
