package org.digit.program.utils;

import org.apache.commons.lang3.StringUtils;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.SortOrder;
import org.digit.program.models.Pagination;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    private final ProgramConfiguration configs;

    public PaginationUtil(ProgramConfiguration configs) {
        this.configs = configs;
    }

    public Pagination enrichSearch(Pagination pagination) {
        if (pagination == null)
            pagination = Pagination.builder().build();

        if (pagination.getLimit() == null) {
            pagination.setLimit(configs.getSearchDefaultLimit());
        } else if (pagination.getLimit() > configs.getSearchMaxLimit()) {
            pagination.setLimit(configs.getSearchMaxLimit());
        }
        if (pagination.getOffset() == null) {
            pagination.setOffset(0);
        }
        if (StringUtils.isEmpty(pagination.getSortBy())) {
            pagination.setSortBy("last_modified_time");
        }
        if (pagination.getSortOrder() == null) {
            pagination.setSortOrder(SortOrder.DESC);
        }
        return pagination;
    }

}
