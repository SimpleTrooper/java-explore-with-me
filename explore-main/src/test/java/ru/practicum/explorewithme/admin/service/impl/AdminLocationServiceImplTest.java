package ru.practicum.explorewithme.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.admin.dto.AdminLocationDto;
import ru.practicum.explorewithme.admin.dto.NewLocationDto;
import ru.practicum.explorewithme.admin.service.AdminLocationService;
import ru.practicum.explorewithme.base.exception.LocationNotFoundException;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.LocationType;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.LocationRepository;
import ru.practicum.explorewithme.base.resolver.LocationGeocodingResolver;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Интеграционные тесты для AdminLocationServiceImpl
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AdminLocationServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class AdminLocationServiceImplTest {
    final LocationRepository locationRepository;
    @InjectMocks
    final AdminLocationService adminLocationService;
    @MockBean
    final LocationGeocodingResolver locationGeocodingResolver;

    Location location1;
    Location location2;

    @BeforeEach
    void init() {
        location1 = Location.builder()
                .lat(53.9507465)
                .lon(49.0344876)
                .radius(4.)
                .name("Вислая Дубрава")
                .description("Поселок в Ульяновской области, Россия")
                .type(LocationType.CITY)
                .resolved(false)
                .build();

        location2 = Location.builder()
                .lat(56.30694604474655)
                .lon(44.002126701432864)
                .radius(15.)
                .name("Нижний новгород")
                .description("Город в России")
                .type(LocationType.CITY)
                .resolved(false)
                .build();

        locationRepository.save(location1);
        locationRepository.save(location2);
    }

    /**
     * Стандартное поведение findAll
     */
    @Test
    void shouldFindAll() {
        List<AdminLocationDto> actual = adminLocationService.findAll(List.of(location1.getId()),
                new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getId(), equalTo(location1.getId()));
        assertThat(actual.get(0).getLat(), equalTo(location1.getLat()));
        assertThat(actual.get(0).getLon(), equalTo(location1.getLon()));
        assertThat(actual.get(0).getRadius(), equalTo(location1.getRadius()));
        assertThat(actual.get(0).getName(), equalTo(location1.getName()));
        assertThat(actual.get(0).getDescription(), equalTo(location1.getDescription()));
        assertThat(actual.get(0).getType(), equalTo(location1.getType().toString()));
    }

    /**
     * Стандартное поведение add
     */
    @Test
    void shouldAdd() {
        NewLocationDto newLocationDto = NewLocationDto.builder()
                .type("CITY")
                .lat(56.239524000532775)
                .lon(43.46140721942966)
                .radius(5.)
                .name("Дзержинск")
                .description("Город в Нижегородской области России")
                .build();

        AdminLocationDto actual = adminLocationService.add(newLocationDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getLat(), equalTo(newLocationDto.getLat()));
        assertThat(actual.getLon(), equalTo(newLocationDto.getLon()));
        assertThat(actual.getRadius(), equalTo(newLocationDto.getRadius()));
        assertThat(actual.getName(), equalTo(newLocationDto.getName()));
        assertThat(actual.getDescription(), equalTo(newLocationDto.getDescription()));
        assertThat(actual.getType(), equalTo(newLocationDto.getType()));

        verify(locationGeocodingResolver, times(1)).resolve(any());
    }

    /**
     * Стандартное поведение update
     */
    @Test
    void shouldUpdate() {
        NewLocationDto updatedLocationDto = NewLocationDto.builder()
                .id(location1.getId())
                .type("CITY")
                .lat(56.239524000532775)
                .lon(43.46140721942966)
                .radius(5.)
                .name("Дзержинск")
                .description("Город в Нижегородской области России")
                .build();

        AdminLocationDto actual = adminLocationService.update(updatedLocationDto);

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), equalTo(location1.getId()));
        assertThat(actual.getLat(), equalTo(updatedLocationDto.getLat()));
        assertThat(actual.getLon(), equalTo(updatedLocationDto.getLon()));
        assertThat(actual.getRadius(), equalTo(updatedLocationDto.getRadius()));
        assertThat(actual.getName(), equalTo(updatedLocationDto.getName()));
        assertThat(actual.getDescription(), equalTo(updatedLocationDto.getDescription()));
        assertThat(actual.getType(), equalTo(updatedLocationDto.getType()));

        verify(locationGeocodingResolver, times(1)).resolve(any());
    }

    /**
     * Стандартное поведение delete
     */
    @Test
    void shouldDelete() {
        assertDoesNotThrow(() -> adminLocationService.delete(location1.getId()));
        assertThrows(LocationNotFoundException.class, () -> adminLocationService.delete(location1.getId()));
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(locationGeocodingResolver);
    }
}