package ru.practicum.explorewithme.base.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Кастомная пагинация, получение первого элемента по смещению from, вместо номера страницы в PageRequest
 */
public class OffsetBasedPageRequest extends PageRequest {
    private final long offset;

    protected OffsetBasedPageRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.offset = from;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    public static OffsetBasedPageRequest of(int from, int size, Sort sort) {
        return new OffsetBasedPageRequest(from, size, sort);
    }

    public static OffsetBasedPageRequest of(int from, int size) {
        return new OffsetBasedPageRequest(from, size, Sort.unsorted());
    }
}
