package ru.practicum.explorewithme.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

/**
 * Реализация бизнес-логики локаций
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminLocationServiceImpl implements AdminLocationService {
    private final LocationRepository locationRepository;
    private final LocationGeocodingResolver locationGeocodingResolver;

    @Autowired
    public AdminLocationServiceImpl(LocationRepository locationRepository,
                                    LocationGeocodingResolver feignClientForGeocode) {
        this.locationRepository = locationRepository;
        this.locationGeocodingResolver = feignClientForGeocode;
    }

    @Override
    public List<AdminLocationDto> findAll(List<Long> locationIds, PaginationRequest paginationRequest) {
        if (locationIds != null && locationIds.size() != 0) {
            return locationRepository.findAllByIdIn(locationIds).stream()
                    .map(AdminLocationDto::from).collect(Collectors.toList());
        }
        return locationRepository.findAll(paginationRequest.makeOffsetBased()).toList()
                .stream()
                .map(AdminLocationDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminLocationDto add(NewLocationDto newLocationDto) {
        Location location = NewLocationDto.toLocation(newLocationDto);
        locationRepository.save(location);
        locationGeocodingResolver.resolve(location);
        return AdminLocationDto.from(location);
    }

    @Override
    @Transactional
    public AdminLocationDto update(NewLocationDto updatedLocationDto) {
        Location location = findByIdOrThrow(updatedLocationDto.getId());
        updateRequiredFields(location, updatedLocationDto);
        return AdminLocationDto.from(location);
    }

    private void updateRequiredFields(Location location, NewLocationDto updatedLocationDto) {
        if (updatedLocationDto.getType() != null) {
            location.setType(LocationType.from(updatedLocationDto.getType()));
        }

        if (updatedLocationDto.getLat() != null) {
            location.setLat(updatedLocationDto.getLat());
        }

        if (updatedLocationDto.getLon() != null) {
            location.setLon(updatedLocationDto.getLon());
        }

        if (updatedLocationDto.getRadius() != null) {
            location.setRadius(updatedLocationDto.getRadius());
        }

        if (updatedLocationDto.getName() != null) {
            location.setName(updatedLocationDto.getName());
        }

        if (updatedLocationDto.getDescription() != null) {
            location.setDescription(updatedLocationDto.getDescription());
        }
    }

    @Override
    @Transactional
    public void delete(Long locationId) {
        findByIdOrThrow(locationId);
        locationRepository.deleteById(locationId);
    }

    @Override
    public AdminLocationDto resolveById(Long locationId) {
        Location location = findByIdOrThrow(locationId);
        locationGeocodingResolver.resolve(location);
        return AdminLocationDto.from(location);
    }

    private Location findByIdOrThrow(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(String.format("Location with id = %d " +
                        "is not found", locationId)));
    }
}
