package ru.practicum.explorewithme.base.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Класс для запросов, содержащих пагинацию
 */
public class PaginationRequest {
    private final Integer from;
    private final Integer size;

    public PaginationRequest(Integer from, Integer size) {
        this.from = from;
        this.size = size;
    }

    public Pageable makeOffsetBasedByFieldDesc(String field) {
        Sort sortingByFieldDesc = Sort.by(Sort.Direction.DESC, field);
        return OffsetBasedPageRequest.of(from, size, sortingByFieldDesc);
    }

    public Pageable makeOffsetBasedByFieldAsc(String field) {
        Sort sortingByFieldAsc = Sort.by(Sort.Direction.ASC, field);
        return OffsetBasedPageRequest.of(from, size, sortingByFieldAsc);
    }

    public Pageable makeOffsetBased() {
        return OffsetBasedPageRequest.of(from, size);
    }
}
