package it.adawant.demo.warehouse.utils;

import it.adawant.demo.warehouse.utils.ObjectsUtils;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class PagedController {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    protected boolean shouldPage(Integer page, Integer size, String sortProperty, Sort.Direction direction) {
        return ObjectsUtils.anyNotNull(page, size, sortProperty, direction);
    }

    protected Pageable extractPageable(Integer page, Integer size, String sortProperty, Sort.Direction direction) {
        val shouldPage = shouldPage(page, size, sortProperty, direction);
        return shouldPage ? createSafelyPageRequest(page, size, sortProperty, direction) : Pageable.unpaged();
    }

    private PageRequest createSafelyPageRequest(Integer page, Integer size, String sortProperty, Sort.Direction direction) {
        if (page == null)
            page = DEFAULT_PAGE;
        if (size == null)
            size = DEFAULT_PAGE_SIZE;
        if (direction == null)
            direction = DEFAULT_SORT_DIRECTION;
        return sortProperty != null ? PageRequest.of(page, size, direction, sortProperty) : PageRequest.of(page, size);
    }

}
