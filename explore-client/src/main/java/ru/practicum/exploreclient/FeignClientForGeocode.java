package ru.practicum.exploreclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.exploreclient.dto.OutputLocationDto;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@PropertySource(value = "/client.properties")
@FeignClient(value = "feignClientForGeocode", url = "${explore.geocode-service.url}")
public interface FeignClientForGeocode {
    @RequestMapping(method = GET, value = "/geocode")
    OutputLocationDto reverseGeocode(@RequestParam Double lat,
                                     @RequestParam Double lon,
                                     @RequestParam String type);
}
