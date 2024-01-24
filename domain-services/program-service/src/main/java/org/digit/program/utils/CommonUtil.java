package org.digit.program.utils;

import org.digit.program.constants.SortOrder;
import org.digit.program.models.Pagination;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public Sort getPagination(Pagination pagination) {
        if (pagination!=null) {
            Sort.Direction direction = Sort.Direction.DESC;
            if (pagination.getSortOrder() != null && pagination.getSortOrder().equals(SortOrder.ASC)) {
                direction = Sort.Direction.ASC;
            }
            Sort sort = null;
            if (pagination.getSortBy() != null)
                sort = Sort.by(new Sort.Order(direction, pagination.getSortBy()));
            return sort;
        }
        return null;
    }

}
