package org.egov.repository.queryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.EatSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class EATQueryBuilder {

    /**
     * @param eatSearchCriteria
     * @return
     */
    public Query buildQuerySearch(EatSearchCriteria eatSearchCriteria) {
        Criteria criteria = Criteria.where("tenantId").is(eatSearchCriteria.getTenantId());

        if (!StringUtils.isEmpty(eatSearchCriteria.getName())) {
            criteria.and("name").is(eatSearchCriteria.getName());
        }

        if (!StringUtils.isEmpty(eatSearchCriteria.getCode())) {
            criteria.and("code").is(eatSearchCriteria.getCode());
        }

        if (eatSearchCriteria.getIds() != null && !eatSearchCriteria.getIds().isEmpty())
            criteria.and("id").in(eatSearchCriteria.getIds());

        return new Query(criteria);
    }
}
