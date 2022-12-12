package ru.practicum.explorewithme.base.resolver;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exploreclient.FeignClientForGeocode;
import ru.practicum.exploreclient.dto.OutputLocationDto;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.util.ExploreDateFormatter;

/**
 * Сервис для получения информации о локации с последующим сохранением
 */
@Slf4j
@Component
public class LocationGeocodingResolver {
    private final FeignClientForGeocode feignClientForGeocode;

    public LocationGeocodingResolver(FeignClientForGeocode feignClientForGeocode) {
        this.feignClientForGeocode = feignClientForGeocode;
    }

    public void resolve(Location location) {
        if (location == null) {
            return;
        }
        OutputLocationDto outputLocationDto = null;
        try {
            outputLocationDto = feignClientForGeocode.reverseGeocode(location.getLat(),
                    location.getLon(),
                    location.getType().toString());
        } catch (FeignException feignException) {
            log.error("Can't resolve location with lat = {} and lon = {}. Ex: {}",
                    location.getLat(),
                    location.getLon(),
                    feignException);
        }
        if (outputLocationDto != null) {
            location.setCountry(outputLocationDto.getCountry());
            location.setState(outputLocationDto.getState());
            location.setCity(outputLocationDto.getCity());
            location.setPostcode(outputLocationDto.getPostalCode());
            location.setStreet(outputLocationDto.getStreet());
            location.setHouseNumber(outputLocationDto.getHouseNumber());
            location.setResolved(outputLocationDto.getResolved() || location.getResolved());
            if (outputLocationDto.getResolved()) {
                location.setResolveDate(ExploreDateFormatter.toLocalDateTime(outputLocationDto.getResolveDate()));
            }
        }
    }
}
