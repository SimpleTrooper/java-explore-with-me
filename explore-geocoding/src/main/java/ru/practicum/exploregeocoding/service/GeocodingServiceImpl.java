package ru.practicum.exploregeocoding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.exploregeocoding.client.GraphHopperClient;
import ru.practicum.exploregeocoding.dto.InputLocation;
import ru.practicum.exploregeocoding.dto.LocationType;
import ru.practicum.exploregeocoding.dto.OutputLocationDto;
import ru.practicum.exploregeocoding.dto.ReverseGeocodeAnswer;
import ru.practicum.util.ExploreDateFormatter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес логики геокодирования
 */
@Service
public class GeocodingServiceImpl implements GeocodingService {
    @Value("${graphhopper.api.key}")
    private String geocodingServiceApiKey;

    private final GraphHopperClient graphHopperClient;

    @Autowired
    public GeocodingServiceImpl(GraphHopperClient graphHopperClient) {
        this.graphHopperClient = graphHopperClient;
    }

    @Override
    public OutputLocationDto reverseGeocode(InputLocation inputLocation) {
        ReverseGeocodeAnswer reverseGeocodeAnswer = graphHopperClient.geocode(geocodingServiceApiKey,
                List.of(inputLocation.getLat(), inputLocation.getLon()),
                true);

        List<ReverseGeocodeAnswer.PlaceInformation> places = reverseGeocodeAnswer.getHits();
        if (places == null || places.isEmpty()) {
            return OutputLocationDto.builder()
                    .resolved(false)
                    .build();
        }
        String resolveDate = ExploreDateFormatter.format(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        switch (LocationType.valueOf(inputLocation.getType())) {
            case COUNTRY:
                return OutputLocationDto.builder()
                        .country(places.get(0).getCountry())
                        .resolveDate(resolveDate)
                        .resolved(true)
                        .build();
            case STATE:
                return OutputLocationDto.builder()
                        .country(places.get(0).getCountry())
                        .state(places.get(0).getState())
                        .resolveDate(resolveDate)
                        .resolved(true)
                        .build();
            case CITY:
                return OutputLocationDto.builder()
                        .country(places.get(0).getCountry())
                        .state(places.get(0).getState())
                        .city(places.get(0).getCity())
                        .resolveDate(resolveDate)
                        .resolved(true)
                        .build();
            case ADDRESS:
                List<ReverseGeocodeAnswer.PlaceInformation> relations  = places.stream()
                        .filter(place -> place.getOsmType() != null && place.getOsmType().equals("R"))
                        .collect(Collectors.toList());
                List<ReverseGeocodeAnswer.PlaceInformation> nodes = places.stream()
                        .filter(place -> place.getOsmType() != null && place.getOsmType().equals("N"))
                        .collect(Collectors.toList());

                places = relations;
                if (relations.isEmpty()) {
                    places = nodes;
                    if (nodes.isEmpty()) {
                        return OutputLocationDto.builder()
                                .resolved(false)
                                .build();
                    }
                }
                return OutputLocationDto.builder()
                        .country(places.get(0).getCountry())
                        .state(places.get(0).getState())
                        .city(places.get(0).getCity())
                        .street(places.get(0).getStreet())
                        .postalCode(places.get(0).getPostcode())
                        .houseNumber(places.get(0).getHousenumber())
                        .resolveDate(resolveDate)
                        .resolved(true)
                        .build();
            case PLACE:
                return OutputLocationDto.builder()
                        .country(places.get(0).getCountry())
                        .state(places.get(0).getState())
                        .city(places.get(0).getCity())
                        .street(places.get(0).getStreet())
                        .postalCode(places.get(0).getPostcode())
                        .houseNumber(places.get(0).getHousenumber())
                        .resolveDate(resolveDate)
                        .resolved(true)
                        .build();
        }
        return OutputLocationDto.builder()
                .country(places.get(0).getCountry())
                .resolveDate(resolveDate)
                .resolved(true)
                .build();
    }
}
