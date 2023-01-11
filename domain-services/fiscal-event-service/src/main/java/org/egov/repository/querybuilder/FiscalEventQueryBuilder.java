package org.egov.repository.querybuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.config.FiscalEventConfiguration;
import org.egov.web.models.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class FiscalEventQueryBuilder {

    @Autowired
    private FiscalEventConfiguration configuration;

    public String buildSearchQuery(Criteria searchCriteria, List<Object> preparedStmtList) {

        StringBuilder query = new StringBuilder("SELECT fiscal_event.*, receivers.receiver, " + "amount.id as amountid, "
                + "amount.fiscaleventid, " + "amount.coaid, " + "amount.frombillingperiod, "
                + "amount.tobillingperiod, " + "amount.amount, " + "amount.coaid, " + "amount.attributes, "
                + "amount.createdtime as amountcreatedtime, " + "amount.createdby as amountcreatedby, "
                + "amount.lastmodifiedtime as amountlastmodifiedtime, "
                + "amount.lastmodifiedby as amountlastmodifiedby " + "FROM eg_ifix_fiscal_event as fiscal_event "
                + "INNER JOIN eg_ifix_amount_detail as amount ON "
                + "fiscal_event.id=amount.fiscaleventid "
                + "LEFT JOIN eg_ifix_receivers as receivers ON "
                + "fiscal_event.id=receivers.fiscaleventid ");


        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.id IN ( ");
            setValuesForList(query, preparedStmtList, searchCriteria.getIds());
            query.append(")");
        }

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if(CollectionUtils.isEmpty(preparedStmtList)){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
        }
    }

    /**
     * Sets prepared statement for values for a list
     *
     * @param query
     * @param preparedStmtList
     * @param ids
     */
    private void setValuesForList(StringBuilder query, List<Object> preparedStmtList, List<String> ids) {
        int len = ids.size();
        for (int i = 0; i < ids.size(); i++) {
            query.append("?");
            if (i != len - 1)
                query.append(", ");
            preparedStmtList.add(ids.get(i));
        }
    }

    public String buildUuidsSearchQuery(Criteria searchCriteria, List<Object> preparedStmtList) {
        String finalIdsQuery = "SELECT id FROM ( {INTERNAL_QUERY} ) AS id";
        StringBuilder query = new StringBuilder("SELECT DISTINCT(fiscal_event.id), fiscal_event.createdtime FROM eg_ifix_fiscal_event as fiscal_event LEFT OUTER JOIN eg_ifix_receivers as receivers ON fiscal_event.id=receivers.fiscaleventid ");

        if (StringUtils.isNotBlank(searchCriteria.getTenantId())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.tenantId = ?");
            preparedStmtList.add(searchCriteria.getTenantId());
        }

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.id IN ( ");
            setValuesForList(query, preparedStmtList, searchCriteria.getIds());
            query.append(")");
        }
        if (StringUtils.isNotBlank(searchCriteria.getEventType())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.eventType = ?");
            preparedStmtList.add(searchCriteria.getEventType());
        }
        if (searchCriteria.getFromEventTime() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.fromEventTime >= ?");
            preparedStmtList.add(searchCriteria.getFromEventTime());
        }
        if (searchCriteria.getToEventTime() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.toEventTime <= ?");
            preparedStmtList.add(searchCriteria.getToEventTime());
        }
        if (searchCriteria.getReferenceId() != null && !searchCriteria.getReferenceId().isEmpty()) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.referenceid IN ( ");
            setValuesForList(query, preparedStmtList, searchCriteria.getReferenceId());
            query.append(")");
        }
        if (StringUtils.isNotBlank(searchCriteria.getReceiver())) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" receivers.receiver = ?");
            preparedStmtList.add(searchCriteria.getReceiver());
        }
        if (searchCriteria.getFromIngestionTime() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.fromIngestionTime >= ?");
            preparedStmtList.add(searchCriteria.getFromIngestionTime());
        }
        if (searchCriteria.getToIngestionTime() != null) {
            addClauseIfRequired(query, preparedStmtList);
            query.append(" fiscal_event.toIngestionTime <= ?");
            preparedStmtList.add(searchCriteria.getToIngestionTime());
        }
        addPaginationAndOrderClause(query, searchCriteria, preparedStmtList);

        return finalIdsQuery.replace("{INTERNAL_QUERY}", query);
    }

    private void addPaginationAndOrderClause(StringBuilder query, Criteria searchCriteria, List<Object> preparedStmtList) {
        // Append default sorting clause
        query.append(" ORDER BY fiscal_event.createdtime DESC ");

        // Append offset
        query.append(" OFFSET ? ");
        preparedStmtList.add(ObjectUtils.isEmpty(searchCriteria.getOffSet()) ? configuration.getDefaultOffset() : searchCriteria.getOffSet());

        // Append limit
        query.append(" LIMIT ? ");
        preparedStmtList.add(ObjectUtils.isEmpty(searchCriteria.getLimit()) ? configuration.getDefaultLimit() : searchCriteria.getLimit());
    }


}
