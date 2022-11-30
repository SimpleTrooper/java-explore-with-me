package ru.practicum.exploregeocoding.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.exploregeocoding.client.GraphHopperClient;
import ru.practicum.exploregeocoding.dto.InputLocation;
import ru.practicum.exploregeocoding.dto.OutputLocationDto;
import ru.practicum.exploregeocoding.dto.ReverseGeocodeAnswer;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Интеграционные тесты для GeocodingServiceImpl
 */
@SpringJUnitConfig({GeocodingServiceImpl.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GeocodingServiceImplTest {
    @InjectMocks
    final GeocodingService geocodingService;
    @MockBean
    final GraphHopperClient graphHopperClient;

    InputLocation inputLocation1;

    @BeforeEach
    void init() {
        inputLocation1 = new InputLocation(53.952008311325216, 49.03485406174002, "ADDRESS");
    }

    /**
     * Стандартное поведение reverseGeocode
     */
    @Test
    void shouldReverseGeocode() {
        ReverseGeocodeAnswer.PlaceInformation place = new ReverseGeocodeAnswer.PlaceInformation();
        place.setState("Ulyanovsk Oblast");
        place.setCountry("Russia");
        place.setPostcode("433423");
        place.setOsmType("N");
        Mockito.when(graphHopperClient.geocode(any(), any(), anyBoolean()))
                .thenReturn(new ReverseGeocodeAnswer(List.of(place), "en"));

        OutputLocationDto actual = geocodingService.reverseGeocode(inputLocation1);

        assertThat(actual, notNullValue());
        assertThat(actual.getCountry(), equalTo("Russia"));
        assertThat(actual.getState(), equalTo("Ulyanovsk Oblast"));
        assertThat(actual.getPostalCode(), equalTo("433423"));

        verify(graphHopperClient, times(1)).geocode(any(), any(), any());
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(graphHopperClient);
    }
}