package ru.practicum.explorewithme.apipublic.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.explorewithme.apipublic.dto.PublicLocationDto;
import ru.practicum.explorewithme.apipublic.service.PublicLocationService;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.model.LocationType;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.LocationRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Интеграционные тесты для PublicLocationServiceImplTest
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PublicLocationServiceImpl.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PublicLocationServiceImplTest {
    final LocationRepository locationRepository;
    final PublicLocationService publicLocationService;

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
        List<PublicLocationDto> actual = publicLocationService.findAll(new PaginationRequest(0, 10));

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0).getId(), equalTo(location1.getId()));
        assertThat(actual.get(0).getLat(), equalTo(location1.getLat()));
        assertThat(actual.get(0).getLon(), equalTo(location1.getLon()));
        assertThat(actual.get(0).getRadius(), equalTo(location1.getRadius()));
        assertThat(actual.get(0).getName(), equalTo(location1.getName()));
        assertThat(actual.get(0).getDescription(), equalTo(location1.getDescription()));
        assertThat(actual.get(0).getType(), equalTo(location1.getType().toString()));
        assertThat(actual.get(1).getId(), equalTo(location2.getId()));
        assertThat(actual.get(1).getLat(), equalTo(location2.getLat()));
        assertThat(actual.get(1).getLon(), equalTo(location2.getLon()));
        assertThat(actual.get(1).getRadius(), equalTo(location2.getRadius()));
        assertThat(actual.get(1).getName(), equalTo(location2.getName()));
        assertThat(actual.get(1).getDescription(), equalTo(location2.getDescription()));
        assertThat(actual.get(1).getType(), equalTo(location2.getType().toString()));
    }

    /**
     * Стандартное поведение findById
     */
    @Test
    void shouldFindById() {

        PublicLocationDto actual = publicLocationService.findById(location1.getId());

        assertThat(actual, notNullValue());
        assertThat(actual.getId(), notNullValue());
        assertThat(actual.getLat(), equalTo(location1.getLat()));
        assertThat(actual.getLon(), equalTo(location1.getLon()));
        assertThat(actual.getRadius(), equalTo(location1.getRadius()));
        assertThat(actual.getName(), equalTo(location1.getName()));
        assertThat(actual.getDescription(), equalTo(location1.getDescription()));
        assertThat(actual.getType(), equalTo(location1.getType().toString()));
    }
}