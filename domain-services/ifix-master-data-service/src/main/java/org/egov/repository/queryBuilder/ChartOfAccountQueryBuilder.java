package org.egov.repository.queryBuilder;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.COASearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChartOfAccountQueryBuilder {


    public Query buildSearchQuery(COASearchCriteria searchCriteria) {

        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());

        if (StringUtils.isNotBlank(searchCriteria.getMajorHead()))
            criteria.and("majorHead").is(searchCriteria.getMajorHead());

        if (StringUtils.isNotBlank(searchCriteria.getMinorHead()))
            criteria.and("minorHead").is(searchCriteria.getMinorHead());

        if (StringUtils.isNotBlank(searchCriteria.getGroupHead()))
            criteria.and("groupHead").is(searchCriteria.getGroupHead());

        if (StringUtils.isNotBlank(searchCriteria.getObjectHead()))
            criteria.and("objectHead").is(searchCriteria.getObjectHead());

        if (StringUtils.isNotBlank(searchCriteria.getSubHead()))
            criteria.and("subHead").is(searchCriteria.getSubHead());

        if (StringUtils.isNotBlank(searchCriteria.getSubMajorHead()))
            criteria.and("subMajorHead").is(searchCriteria.getSubMajorHead());

        if (searchCriteria.getCoaCodes() != null && !searchCriteria.getCoaCodes().isEmpty())
            criteria.and("coaCode").in(searchCriteria.getCoaCodes());

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty())
            criteria.and("id").in(searchCriteria.getIds());

        return new Query(criteria);
    }
}
