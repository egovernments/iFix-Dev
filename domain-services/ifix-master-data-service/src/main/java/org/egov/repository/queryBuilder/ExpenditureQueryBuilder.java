package org.egov.repository.queryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.egov.util.MasterDataConstants;
import org.egov.web.models.ExpenditureSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ExpenditureQueryBuilder {

    /**
     * @param expenditureSearchCriteria
     * @return
     */
    public Query buildQuerySearch(ExpenditureSearchCriteria expenditureSearchCriteria) {
        Criteria criteria = Criteria.where(MasterDataConstants.CRITERIA_TENANT_ID).is(expenditureSearchCriteria.getTenantId());

        if (!StringUtils.isEmpty(expenditureSearchCriteria.getName())) {
            criteria.and(MasterDataConstants.CRITERIA_NAME).is(expenditureSearchCriteria.getName());
        }

        if (!StringUtils.isEmpty(expenditureSearchCriteria.getCode())) {
            criteria.and(MasterDataConstants.CRITERIA_CODE).is(expenditureSearchCriteria.getCode());
        }

        if (expenditureSearchCriteria.getIds() != null && !expenditureSearchCriteria.getIds().isEmpty())
            criteria.and(MasterDataConstants.CRITERIA_ID).in(expenditureSearchCriteria.getIds());

        return new Query(criteria);
    }
}
