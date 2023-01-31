package org.egov.repository.queryBuilder;

import lombok.extern.slf4j.Slf4j;
import org.egov.repository.criteriaBuilder.QueryCriteria;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.ExpenditureSearchCriteria;
import org.egov.web.models.persist.Department;
import org.egov.web.models.persist.Expenditure;
import org.springframework.stereotype.Component;

import static org.egov.util.MasterDataConstants.ExpenditureConst.*;


/**
 *
 */
@Component
@Slf4j
public class ExpenditureQueryBuilder {


    /**
     * @param expenditureSearchCriteria
     * @return
     */
    public String buildSearchQuery(ExpenditureSearchCriteria expenditureSearchCriteria) {
        QueryCriteria queryCriteria = QueryCriteria.builder(Expenditure.class);

        queryCriteria.where(TENANT_ID).is(expenditureSearchCriteria.getTenantId());

        if (!org.springframework.util.StringUtils.isEmpty(expenditureSearchCriteria.getName())) {
            queryCriteria.and(NAME).is(expenditureSearchCriteria.getName());
        }

        if (!org.springframework.util.StringUtils.isEmpty(expenditureSearchCriteria.getCode())) {
            queryCriteria.and(CODE).is(expenditureSearchCriteria.getCode());
        }

        if (expenditureSearchCriteria.getIds() != null && !expenditureSearchCriteria.getIds().isEmpty()) {
            queryCriteria.and(ID).in(expenditureSearchCriteria.getIds());
        }

        return queryCriteria.build();
    }
}
