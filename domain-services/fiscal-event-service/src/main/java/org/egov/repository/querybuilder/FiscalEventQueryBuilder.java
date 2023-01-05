package org.egov.repository.querybuilder;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.PlainsearchCriteria;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FiscalEventQueryBuilder {

    public Query buildSearchQuery(org.egov.web.models.Criteria searchCriteria) {

        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());

        if (StringUtils.isNotBlank(searchCriteria.getEventType()))
            criteria.and("eventType").is(searchCriteria.getEventType());

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty())
            criteria.and("id").in(searchCriteria.getIds());

        if (searchCriteria.getReferenceId() != null && !searchCriteria.getReferenceId().isEmpty())
            criteria.and("referenceId").in(searchCriteria.getReferenceId());

        if (searchCriteria.getFromEventTime() != null && searchCriteria.getToEventTime() != null)
            criteria.and("eventTime").gte(searchCriteria.getFromEventTime()).lte(searchCriteria.getToEventTime());

        if (StringUtils.isNotBlank(searchCriteria.getReceiver()))
            criteria.and("receivers").is(searchCriteria.getReceiver());

        if (searchCriteria.getFromIngestionTime() != null && searchCriteria.getToIngestionTime() != null)
            criteria.and("ingestionTime").gte(searchCriteria.getFromIngestionTime()).lte(searchCriteria.getToIngestionTime());

        return new Query(criteria);
    }

    public Query buildPlainSearchQuery(PlainsearchCriteria searchCriteria) {
        Criteria criteria = Criteria.where("tenantId").is(searchCriteria.getTenantId());
        Query finalQuery = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "ingestionTime"));

        if(!searchCriteria.getIsCountCall()) {
            Pageable pageableRequest = PageRequest.of(searchCriteria.getOffset(), searchCriteria.getLimit());
            return finalQuery.with(pageableRequest);
        }

        return finalQuery;
    }
}
