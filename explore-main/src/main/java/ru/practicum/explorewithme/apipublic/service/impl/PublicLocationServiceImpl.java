package ru.practicum.explorewithme.apipublic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.apipublic.dto.PublicLocationDto;
import ru.practicum.explorewithme.apipublic.service.PublicLocationService;
import ru.practicum.explorewithme.base.exception.LocationNotFoundException;
import ru.practicum.explorewithme.base.model.Location;
import ru.practicum.explorewithme.base.pagination.PaginationRequest;
import ru.practicum.explorewithme.base.repository.LocationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация бизнес логики локаций публичного API
 */
@Service
@Transactional(readOnly = true)
public class PublicLocationServiceImpl implements PublicLocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public PublicLocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<PublicLocationDto> findAll(PaginationRequest paginationRequest) {
        return locationRepository.findAll(paginationRequest.makeOffsetBased()).stream()
                .map(PublicLocationDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public PublicLocationDto findById(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(String.format("Location with id = %d " +
                        "is not found", locationId)));
        return PublicLocationDto.from(location);
    }
}
